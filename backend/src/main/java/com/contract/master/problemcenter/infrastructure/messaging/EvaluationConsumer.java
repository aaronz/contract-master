package com.contract.master.problemcenter.infrastructure.messaging;

import com.contract.master.contract.domain.model.Contract;
import com.contract.master.contract.domain.repository.ContractRepository;
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
    private final List<RuleExecutor> ruleExecutors;

    public EvaluationConsumer(ProblemEvaluationJobRepository jobRepository,
                              RuleRepository ruleRepository,
                              ProblemRepository problemRepository,
                              ContractRepository contractRepository,
                              List<RuleExecutor> ruleExecutors) {
        this.jobRepository = jobRepository;
        this.ruleRepository = ruleRepository;
        this.problemRepository = problemRepository;
        this.contractRepository = contractRepository;
        this.ruleExecutors = ruleExecutors;
    }

    @KafkaListener(topics = "contract-evaluation", groupId = "contract-master-group")
    @Transactional
    public void consume(String jobIdStr) {
        Long jobId = Long.valueOf(jobIdStr);
        jobRepository.findById(jobId).ifPresent(job -> {
            try {
                com.contract.master.security.TenantContext.setCurrentTenant(job.getTenantId().getId());
                executeJob(job);
            } finally {
                com.contract.master.security.TenantContext.clear();
            }
        });
    }

    private void executeJob(ProblemEvaluationJob job) {
        try {
            job.start();
            jobRepository.save(job);

            Contract contract = contractRepository.findById(new com.contract.master.contract.domain.model.ContractId(job.getContractId()))
                    .orElseThrow(() -> new RuntimeException("Contract not found: " + job.getContractId()));

            List<Rule> rules = ruleRepository.findByTenantIdAndStatus(job.getTenantId(), RuleStatus.ACTIVE);

            Map<String, Object> facts = new HashMap<>();
            facts.put("contract", contract);
            facts.put("content", contract.getContractName());

            List<Problem> problems = rules.stream()
                    .map(rule -> {
                        RuleExecutionResult result = getExecutor(rule.getLogicType()).execute(rule, facts);
                        if (result.isMatched()) {
                            return createProblem(job, rule, contract, result);
                        }
                        return null;
                    })
                    .filter(java.util.Objects::nonNull)
                    .collect(Collectors.toList());

            problemRepository.saveAll(problems);
            job.complete();
            jobRepository.save(job);

        } catch (Exception e) {
            log.error("Failed to execute evaluation job: {}", job.getId(), e);
            job.fail(e.getMessage());
            jobRepository.save(job);
        }
    }

    private RuleExecutor getExecutor(RuleLogicType type) {
        String prefix = type.name().toLowerCase();
        return ruleExecutors.stream()
                .filter(e -> e.getClass().getSimpleName().toLowerCase().startsWith(prefix))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No executor found for logic type: " + type));
    }

    private Problem createProblem(ProblemEvaluationJob job, Rule rule, Contract contract, RuleExecutionResult result) {
        Problem problem = new Problem();
        problem.setEvaluationJobId(job.getId());
        problem.setRuleId(rule.getId());
        problem.setContractId(contract.getContractId().value());
        problem.setTenantId(job.getTenantId());
        problem.setGeneratedMessage("Rule hit: " + rule.getName());
        problem.setStatus(ProblemStatus.NEW);
        if (result.getLocation() != null) {
            problem.setLocationInContract(result.getLocation().toJson());
        }
        return problem;
    }
}
