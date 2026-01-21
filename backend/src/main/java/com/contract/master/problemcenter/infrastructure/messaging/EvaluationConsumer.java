package com.contract.master.problemcenter.infrastructure.messaging;

import com.contract.master.contract.domain.model.Contract;
import com.contract.master.contract.domain.repository.ContractRepository;
import com.contract.master.contract.dto.ContractDTO;
import com.contract.master.contract.application.ContractService;
import com.contract.master.problemcenter.domain.model.ProblemEvaluationJob;
import com.contract.master.problemcenter.domain.model.ProblemEvaluationJobStatus;
import com.contract.master.problemcenter.domain.model.Problem;
import com.contract.master.problemcenter.domain.model.ProblemStatus;
import com.contract.master.problemcenter.domain.repository.ProblemEvaluationJobRepository;
import com.contract.master.problemcenter.domain.repository.ProblemRepository;
import com.contract.master.rule.domain.model.Rule;
import com.contract.master.rule.domain.model.RuleLogicType;
import com.contract.master.rule.domain.model.RuleStatus;
import com.contract.master.rule.domain.repository.RuleRepository;
import com.contract.master.rule.infrastructure.script.RuleExecutionResult;
import com.contract.master.rule.infrastructure.script.RuleExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class EvaluationConsumer {

    private static final Logger log = LoggerFactory.getLogger(EvaluationConsumer.class);

    private final ProblemEvaluationJobRepository jobRepository;
    private final RuleRepository ruleRepository;
    private final ProblemRepository problemRepository;
    private final ContractRepository contractRepository;
    private final ContractService contractService;
    private final List<RuleExecutor> ruleExecutors;
    private final com.contract.master.notification.application.NotificationService notificationService;

    public EvaluationConsumer(ProblemEvaluationJobRepository jobRepository,
                              RuleRepository ruleRepository,
                              ProblemRepository problemRepository,
                              ContractRepository contractRepository,
                              ContractService contractService,
                              List<RuleExecutor> ruleExecutors,
                              com.contract.master.notification.application.NotificationService notificationService) {
        this.jobRepository = jobRepository;
        this.ruleRepository = ruleRepository;
        this.problemRepository = problemRepository;
        this.contractRepository = contractRepository;
        this.contractService = contractService;
        this.ruleExecutors = ruleExecutors;
        this.notificationService = notificationService;
    }

    @KafkaListener(topics = "contract-evaluation", groupId = "contract-master-group")
    @Transactional
    public void consume(String jobIdStr) {
        try {
            Long jobId = Long.valueOf(jobIdStr);
            jobRepository.findById(jobId).ifPresent(job -> {
                try {
                    com.contract.master.security.TenantContext.setCurrentTenant(job.getTenantId().getId());
                    executeJob(job);
                } finally {
                    com.contract.master.security.TenantContext.clear();
                }
            });
        } catch (Exception e) {
            log.error("Failed to consume evaluation job message: {}", jobIdStr, e);
        }
    }

    private void executeJob(ProblemEvaluationJob job) {
        try {
            log.info("Starting evaluation job {} for contract {}", job.getId(), job.getContractId());
            job.start();
            jobRepository.save(job);

            Contract contract = contractRepository.findById(new com.contract.master.contract.domain.model.ContractId(job.getContractId()))
                    .orElseThrow(() -> new RuntimeException("Contract not found: " + job.getContractId()));

            ContractDTO contractDTO = contractService.convertToDTO(contract);
            List<Rule> rules = ruleRepository.findByStatus(RuleStatus.ACTIVE);
            
            if (job.getRuleIdsJson() != null && !job.getRuleIdsJson().isEmpty()) {
                try {
                    com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
                    List<String> ruleIds = mapper.readValue(job.getRuleIdsJson(), new com.fasterxml.jackson.core.type.TypeReference<List<String>>(){});
                    rules = rules.stream().filter(r -> ruleIds.contains(r.getId().toString())).collect(Collectors.toList());
                } catch (Exception e) {
                    log.error("Failed to parse rule IDs for job {}", job.getId(), e);
                }
            }
            
            log.info("Found {} rules to execute for tenant {}", rules.size(), job.getTenantId().getId());

            Map<String, Object> facts = new HashMap<>();
            facts.put("contract", contractDTO);
            facts.put("content", contract.getContractName());
            
            facts.put("contractNo", contractDTO.getContractNo());
            facts.put("contractName", contractDTO.getContractName());
            facts.put("contractType", contractDTO.getContractType());
            facts.put("contractAmount", contractDTO.getContractAmount());
            facts.put("totalAmountWithTax", contractDTO.getTotalAmountWithTax());
            facts.put("contractStatus", contractDTO.getContractStatus());
            facts.put("approvalStatus", contractDTO.getApprovalStatus());
            
            if (contractDTO.getExtendedFields() != null) {
                facts.putAll(contractDTO.getExtendedFields());
            }

            String contractIdStr = contract.getContractId().value().toString();
            problemRepository.deleteByContractId(contractIdStr);

            List<Problem> problems = rules.stream()
                    .map(rule -> {
                        try {
                            log.debug("Executing rule: {} ({})", rule.getName(), rule.getLogicType());
                            RuleExecutionResult result = getExecutor(rule.getLogicType()).execute(rule, facts);
                            if (result.isMatched()) {
                                log.info("Rule matched: {} for contract {}", rule.getName(), contractIdStr);
                                return createProblem(job, rule, contract, result, facts);
                            }
                        } catch (Exception e) {
                            log.error("Error executing rule {} for contract {}", rule.getId(), contractIdStr, e);
                        }
                        return null;
                    })
                    .filter(java.util.Objects::nonNull)
                    .collect(Collectors.toList());

            if (!problems.isEmpty()) {
                log.info("Saving {} problems for contract {}", problems.size(), contractIdStr);
                problemRepository.saveAll(problems);
                
                notificationService.sendNotification(
                    "admin", 
                    "Compliance Issues Detected", 
                    String.format("System found %d issues in contract %s during automated evaluation.", 
                        problems.size(), contract.getContractName()),
                    "COMPLIANCE_ALERT"
                );
            } else {
                log.info("No compliance issues detected for contract {}", contractIdStr);
            }
            
            job.complete();
            jobRepository.save(job);

            log.info("Evaluation job {} completed.", job.getId());

        } catch (Exception e) {
            log.error("Failed to execute evaluation job: {}", job.getId(), e);
            job.fail(e.getMessage());
            jobRepository.save(job);
        }
    }

    private RuleExecutor getExecutor(RuleLogicType type) {
        String typeName = type.name().toLowerCase().replace("_", "");
        return ruleExecutors.stream()
                .filter(e -> e.getClass().getSimpleName().toLowerCase().contains(typeName))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No executor found for logic type: " + type));
    }

    private Problem createProblem(ProblemEvaluationJob job, Rule rule, Contract contract, RuleExecutionResult result, Map<String, Object> facts) {
        Problem problem = new Problem();
        problem.setEvaluationJobId(job.getId());
        problem.setRuleId(rule.getId());
        problem.setContractId(contract.getContractId().value().toString());
        
        String message = "Rule hit: " + rule.getName();
        if (rule.getDescription() != null && !rule.getDescription().isEmpty()) {
            message += ". " + rule.getDescription();
        }
        problem.setGeneratedMessage(message);
        
        problem.setStatus(ProblemStatus.NEW);
        problem.setSeverity(rule.getSeverity());
        if (result.getLocation() != null) {
            problem.setLocationInContract(result.getLocation().toJson());
        }
        return problem;
    }
}
