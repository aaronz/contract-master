package com.contract.master.evaluation.application;

import com.contract.master.evaluation.domain.model.EvaluationJob;
import com.contract.master.evaluation.domain.model.EvaluationResult;
import com.contract.master.evaluation.domain.model.ResultStatus;
import com.contract.master.evaluation.domain.model.JobStatus;
import com.contract.master.evaluation.domain.repository.EvaluationJobRepository;
import com.contract.master.evaluation.domain.repository.EvaluationResultRepository;
import com.contract.master.evaluation.domain.service.RuleEngineDomainService;
import com.contract.master.evaluation.infrastructure.messaging.KafkaProducerService;
import com.contract.master.audit.application.AuditService;
import com.contract.master.contract.dto.ContractDTO;
import com.contract.master.contract.application.ContractService;
import com.contract.master.security.TenantContext;
import com.contract.master.shared.domain.model.TenantId;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class EvaluationApplicationService {

    private static final Logger logger = LoggerFactory.getLogger(EvaluationApplicationService.class);

    private final EvaluationJobRepository jobRepository;
    private final EvaluationResultRepository resultRepository;
    private final KafkaProducerService kafkaProducerService;
    private final AuditService auditService;
    private final RuleEngineDomainService ruleEngineDomainService;
    private final ContractService contractService;
    private final ObjectMapper objectMapper;

    public EvaluationApplicationService(EvaluationJobRepository jobRepository,
                                     EvaluationResultRepository resultRepository,
                                     KafkaProducerService kafkaProducerService,
                                     AuditService auditService,
                                     RuleEngineDomainService ruleEngineDomainService,
                                     ContractService contractService,
                                     ObjectMapper objectMapper) {
        this.jobRepository = jobRepository;
        this.resultRepository = resultRepository;
        this.kafkaProducerService = kafkaProducerService;
        this.auditService = auditService;
        this.ruleEngineDomainService = ruleEngineDomainService;
        this.contractService = contractService;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "evaluation-jobs", groupId = "evaluation-group")
    public void listen(String message) {
        try {
            logger.info("Received evaluation job message: {}", message);
            String jobId = message;

            jobRepository.findByJobId(jobId).ifPresentOrElse(job -> {
                try {
                    job.start();
                    jobRepository.save(job);

                    List<String> contractIds = objectMapper.readValue(job.getTargetContracts(), new TypeReference<List<String>>(){});
                    List<String> ruleIds = objectMapper.readValue(job.getTargetRules(), new TypeReference<List<String>>(){});

                    int processedCount = 0;
                    for (String contractId : contractIds) {
                        try {
                            ContractDTO contract = contractService.getContractById(contractId);
                            if (contract != null) {
                                List<String> violations = ruleEngineDomainService.validate(contract, ruleIds);
                                
                                EvaluationResult result;
                                if (violations.isEmpty()) {
                                    result = EvaluationResult.pass(job.getTenantId(), job.getJobId(), "batch-rules", contractId, "Passed all rules");
                                } else {
                                    result = EvaluationResult.fail(job.getTenantId(), job.getJobId(), "batch-rules", contractId, String.join("; ", violations));
                                }
                                result.setResultId(UUID.randomUUID().toString());
                                resultRepository.save(result);
                                processedCount++;
                            }
                        } catch (Exception e) {
                            logger.error("Failed to evaluate contract {}", contractId, e);
                        }
                    }

                    job.complete();
                    jobRepository.save(job);

                    logger.info("Evaluation job {} completed. Processed {} contracts.", jobId, processedCount);
                } catch (Exception e) {
                    logger.error("Error processing evaluation job {}", jobId, e);
                    job.fail();
                    jobRepository.save(job);
                }
            }, () -> {
                logger.warn("Evaluation job with ID {} not found", jobId);
            });
        } catch (Exception e) {
            logger.error("Unexpected error in Kafka listener for message: {}", message, e);
        }
    }

    public EvaluationJob createAndPublishEvaluationJob(List<String> ruleIds, List<String> contractIds, String tenantId, String triggeredBy) {
        EvaluationJob job = new EvaluationJob(tenantId, EvaluationJob.TriggerType.MANUAL, triggeredBy);
        job.setJobId(UUID.randomUUID().toString());
        try {
            job.setTargetRules(objectMapper.writeValueAsString(ruleIds));
            job.setTargetContracts(objectMapper.writeValueAsString(contractIds));
        } catch (Exception e) {
            logger.error("Failed to serialize job targets", e);
            throw new RuntimeException("Job creation failed due to serialization error");
        }
        
        jobRepository.save(job);

        auditService.logChange("JOB-" + job.getJobId(), "EvaluationJob", null, "Created", "CREATE", triggeredBy);
        logger.info("Manual Evaluation Job Created - Job ID: {}, Tenant ID: {}", job.getJobId(), job.getTenantId().getId());

        kafkaProducerService.sendMessage(job.getJobId());
        return job;
    }

    @Transactional
    public String triggerReEvaluationForSingleContract(String contractId, List<String> ruleIds, String triggeredBy) {
        if (contractService.getContractById(contractId) == null) {
            throw new IllegalArgumentException("Contract with ID " + contractId + " not found.");
        }

        String contractIdJson;
        try {
            contractIdJson = objectMapper.writeValueAsString(List.of(contractId));
        } catch (Exception e) {
            logger.error("Failed to serialize contractId for conflict check", e);
            throw new RuntimeException("Re-evaluation failed due to internal error.");
        }
        
        List<EvaluationJob> inProgressJobs = jobRepository.findByTargetContractsContainingAndStatus(
                contractIdJson, JobStatus.IN_PROGRESS);

        if (!inProgressJobs.isEmpty()) {
            throw new IllegalStateException("An evaluation for this contract is already in progress.");
        }

        EvaluationJob job = createAndPublishEvaluationJob(ruleIds, List.of(contractId), null, triggeredBy);
        
        String rulesStr = ruleIds != null ? String.join(",", ruleIds) : "all";
        auditService.logReEvaluationTriggered(contractId, rulesStr, triggeredBy);

        return job.getJobId();
    }
}
