package com.contractmaster.evaluation.service;

import com.contractmaster.evaluation.model.EvaluationJob;
import com.contractmaster.evaluation.model.EvaluationResult;
import com.contractmaster.evaluation.repository.EvaluationJobRepository;
import com.contractmaster.evaluation.repository.EvaluationResultRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List; // Added import for List

@Service
public class EvaluationService {

    private final EvaluationJobRepository jobRepository;
    private final EvaluationResultRepository resultRepository;
    private final KafkaProducerService kafkaProducerService; // Added KafkaProducerService field

    public EvaluationService(EvaluationJobRepository jobRepository, EvaluationResultRepository resultRepository, KafkaProducerService kafkaProducerService) {
        this.jobRepository = jobRepository;
        this.resultRepository = resultRepository;
        this.kafkaProducerService = kafkaProducerService;
    }

    @KafkaListener(topics = "evaluation-jobs", groupId = "evaluation-group")
    public void listen(String message) {
        // In a real application, 'message' would be a serialized object (e.g., JSON)
        // representing the evaluation job details. For this task, we'll simulate processing.
        System.out.println("Received Message in group evaluation-group: " + message);

        // Simulate processing an evaluation job
        // This is a placeholder. Real logic would involve:
        // 1. Deserializing the message to get EvaluationJob details and rules/contracts to evaluate.
        // 2. Fetching rules and contracts from their respective services/repositories.
        // 3. Invoking the actual rule engine.
        // 4. Storing individual EvaluationResults.
        // 5. Updating the EvaluationJob status.

        // For demonstration, let's assume the message contains a jobId and simulate a result.
        // In a real scenario, the message should contain enough info to retrieve the job and
        // perform the actual evaluation.
        String jobId = message; // Placeholder: assume message is jobId for now.

        jobRepository.findById(jobId).ifPresent(job -> {
            job.setStatus(EvaluationJob.JobStatus.IN_PROGRESS);
            jobRepository.save(job);

            // Simulate creating some results
            // This part needs to be refined when actual rule/contract data is available
            EvaluationResult result = new EvaluationResult(
                job.getTenantId(),
                job.getId(),
                "rule-simulated-id", // Placeholder rule ID
                "contract-simulated-id", // Placeholder contract ID
                EvaluationResult.ResultStatus.PASS, // Simulated result
                "Simulated evaluation passed for rule-simulated-id against contract-simulated-id",
                LocalDateTime.now()
            );
            resultRepository.save(result);

            job.setStatus(EvaluationJob.JobStatus.COMPLETED);
            job.setCompletedAt(LocalDateTime.now());
            jobRepository.save(job);
        });
    }

    // This method will be used by the controller to trigger new evaluations
    // Actual implementation to be done in T013
    public EvaluationJob createAndPublishEvaluationJob(List<String> ruleIds, List<String> contractIds, String tenantId, String triggeredBy) {
        EvaluationJob job = new EvaluationJob(tenantId, EvaluationJob.JobStatus.PENDING, EvaluationJob.TriggerType.MANUAL, LocalDateTime.now(), triggeredBy);
        // In a real scenario, ruleIds and contractIds would be persisted with the job or passed in the Kafka message
        jobRepository.save(job); // Save the job to get an ID and persist initial state

        // TODO: Implement proper audit logging mechanism
        System.out.println("AUDIT: Manual Evaluation Job Created - Job ID: " + job.getId() +
                           ", Tenant ID: " + tenantId + ", Triggered By: " + triggeredBy +
                           ", Rules: " + String.join(",", ruleIds) +
                           ", Contracts: " + String.join(",", contractIds));

        // In a real scenario, the message sent to Kafka would contain more details like
        // ruleIds, contractIds, tenantId, jobId for the consumer to process.
        // For now, we'll just send the jobId to Kafka and assume consumer knows how to fetch job details.
        kafkaProducerService.sendMessage(job.getId());
        return job;
    }
}
