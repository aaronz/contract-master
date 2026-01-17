package com.contract.master.evaluation.service;

import com.contract.master.evaluation.model.EvaluationJob;
import com.contract.master.evaluation.model.EvaluationResult;
import com.contract.master.evaluation.repository.EvaluationJobRepository;
import com.contract.master.evaluation.repository.EvaluationResultRepository;
import com.contract.master.service.AuditService;
import com.contract.master.dto.ContractDTO;
import com.contract.master.service.ContractService;
import com.contract.master.service.RuleEngineService;
import com.contract.master.security.TenantContext; // Import TenantContext
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Import Transactional

import java.time.LocalDateTime;
import java.util.List;
import java.util.Arrays; // Needed for List.of
import java.util.Optional; // Needed for findById

@Service
public class EvaluationService {

    private static final Logger logger = LoggerFactory.getLogger(EvaluationService.class);

    private final EvaluationJobRepository jobRepository;
    private final EvaluationResultRepository resultRepository;
    private final KafkaProducerService kafkaProducerService;
    private final AuditService auditService;
    private final RuleEngineService ruleEngineService;
    private final ContractService contractService;
    private final ObjectMapper objectMapper;

    public EvaluationService(EvaluationJobRepository jobRepository,
                             EvaluationResultRepository resultRepository,
                             KafkaProducerService kafkaProducerService,
                             AuditService auditService,
                             RuleEngineService ruleEngineService,
                             ContractService contractService,
                             ObjectMapper objectMapper) {
        this.jobRepository = jobRepository;
        this.resultRepository = resultRepository;
        this.kafkaProducerService = kafkaProducerService;
        this.auditService = auditService;
        this.ruleEngineService = ruleEngineService;
        this.contractService = contractService;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "evaluation-jobs", groupId = "evaluation-group")
    public void listen(String message) {
        try {
            logger.info("Received evaluation job message: {}", message);
            String jobId = message;

            jobRepository.findById(jobId).ifPresentOrElse(job -> {
                try {
                    job.setStatus(EvaluationJob.JobStatus.IN_PROGRESS);
                    jobRepository.save(job);

                    List<String> contractIds = objectMapper.readValue(job.getTargetContracts(), new TypeReference<List<String>>(){});
                    List<String> ruleIds = objectMapper.readValue(job.getTargetRules(), new TypeReference<List<String>>(){});

                    int processedCount = 0;
                    for (String contractId : contractIds) {
                        try {
                            ContractDTO contract = contractService.getContractById(contractId);
                            if (contract != null) {
                                List<String> violations = ruleEngineService.validate(contract, ruleIds);
                                EvaluationResult.ResultStatus status = violations.isEmpty() ? EvaluationResult.ResultStatus.PASS : EvaluationResult.ResultStatus.FAIL;
                                String resultDetail = violations.isEmpty() ? "Passed all rules" : String.join("; ", violations);

                                EvaluationResult result = new EvaluationResult(
                                    job.getTenantId(),
                                    job.getId(),
                                    "batch-rules",
                                    contractId,
                                    status,
                                    resultDetail,
                                    LocalDateTime.now()
                                );
                                resultRepository.save(result);
                                processedCount++;
                            }
                        } catch (Exception e) {
                            logger.error("Failed to evaluate contract {}", contractId, e);
                        }
                    }

                    job.setStatus(EvaluationJob.JobStatus.COMPLETED);
                    job.setCompletedAt(LocalDateTime.now());
                    jobRepository.save(job);

                    logger.info("Evaluation job {} completed. Processed {} contracts.", jobId, processedCount);
                } catch (Exception e) {
                    logger.error("Error processing evaluation job {}", jobId, e);
                    job.setStatus(EvaluationJob.JobStatus.FAILED);
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
        EvaluationJob job = new EvaluationJob(tenantId, EvaluationJob.JobStatus.PENDING, EvaluationJob.TriggerType.MANUAL, LocalDateTime.now(), triggeredBy);
        try {
            job.setTargetRules(objectMapper.writeValueAsString(ruleIds));
            job.setTargetContracts(objectMapper.writeValueAsString(contractIds));
        } catch (Exception e) {
            logger.error("Failed to serialize job targets", e);
            throw new RuntimeException("Job creation failed due to serialization error");
        }
        
        jobRepository.save(job);

        String description = "Rules: " + String.join(",", ruleIds) + ", Contracts: " + String.join(",", contractIds);
        auditService.logChange("JOB-" + job.getId(), "EvaluationJob", null, "Created", "CREATE", triggeredBy);
        logger.info("Manual Evaluation Job Created - Job ID: {}, Tenant ID: {}", job.getId(), tenantId);

        kafkaProducerService.sendMessage(job.getId());
        return job;
    }

    @Transactional
    public String triggerReEvaluationForSingleContract(String contractId, List<String> ruleIds, String triggeredBy) {
        String tenantId = com.contract.master.security.TenantContext.getCurrentTenant();

        // 1. Validate contract existence
        contractService.getContractById(contractId); // Throws exception if not found

        // 2. Check for existing in-progress evaluations for the contract
        // Construct JSON-like string for query
        String contractIdJson;
        try {
            contractIdJson = objectMapper.writeValueAsString(List.of(contractId)); // Ensures exact match for single item array
        } catch (Exception e) {
            logger.error("Failed to serialize contractId for conflict check", e);
            throw new RuntimeException("Re-evaluation failed due to internal error.");
        }
        
        List<EvaluationJob> inProgressJobs = jobRepository.findByTenantIdAndTargetContractsContainingAndStatus(
                tenantId, contractIdJson, EvaluationJob.JobStatus.IN_PROGRESS);

        if (!inProgressJobs.isEmpty()) {
            throw new IllegalStateException("An evaluation for this contract is already in progress.");
        }

        // 3. Use the existing createAndPublishEvaluationJob
        EvaluationJob job = createAndPublishEvaluationJob(ruleIds, List.of(contractId), tenantId, triggeredBy);
        
        // 4. Record audit log entry (FR-008) - createAndPublishEvaluationJob already logs a generic CREATE event.
        //    For a more specific "RE_EVALUATION_TRIGGERED" audit event, add it here.
        auditService.logReEvaluationTriggered(contractId, String.join(",", ruleIds), triggeredBy);

        return job.getId();
    }
}
