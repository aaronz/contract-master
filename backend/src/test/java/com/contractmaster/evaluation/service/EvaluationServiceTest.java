package com.contractmaster.evaluation.service;

import com.contractmaster.evaluation.model.EvaluationJob;
import com.contractmaster.evaluation.model.EvaluationResult;
import com.contractmaster.evaluation.repository.EvaluationJobRepository;
import com.contractmaster.evaluation.repository.EvaluationResultRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EvaluationServiceTest {

    @Mock
    private EvaluationJobRepository jobRepository;
    @Mock
    private EvaluationResultRepository resultRepository;
    @Mock
    private KafkaProducerService kafkaProducerService; // Mock the KafkaProducerService

    @InjectMocks
    private EvaluationService evaluationService;

    private EvaluationJob testJob;
    private List<String> ruleIds;
    private List<String> contractIds;
    private String tenantId;
    private String triggeredBy;

    @BeforeEach
    void setUp() {
        tenantId = "test-tenant";
        triggeredBy = "test-user";
        ruleIds = Arrays.asList("rule1", "rule2");
        contractIds = Arrays.asList("contract1", "contract2");

        testJob = new EvaluationJob(tenantId, EvaluationJob.JobStatus.PENDING, EvaluationJob.TriggerType.MANUAL, LocalDateTime.now(), triggeredBy);
        testJob.setId("job-123");
    }

    @Test
    void testCreateAndPublishEvaluationJob() {
        when(jobRepository.save(any(EvaluationJob.class))).thenReturn(testJob);

        EvaluationJob createdJob = evaluationService.createAndPublishEvaluationJob(ruleIds, contractIds, tenantId, triggeredBy);

        assertNotNull(createdJob);
        assertEquals(testJob.getId(), createdJob.getId());
        assertEquals(EvaluationJob.JobStatus.PENDING, createdJob.getStatus());
        verify(jobRepository, times(1)).save(any(EvaluationJob.class));
        verify(kafkaProducerService, times(1)).sendMessage(testJob.getId()); // Verify Kafka message is sent
    }

    @Test
    void testListen_jobCompletion() {
        when(jobRepository.findById(testJob.getId())).thenReturn(Optional.of(testJob));
        when(jobRepository.save(any(EvaluationJob.class))).thenReturn(testJob);
        when(resultRepository.save(any(EvaluationResult.class))).thenReturn(new EvaluationResult());

        evaluationService.listen(testJob.getId());

        assertEquals(EvaluationJob.JobStatus.COMPLETED, testJob.getStatus());
        assertNotNull(testJob.getCompletedAt());
        verify(jobRepository, times(2)).save(any(EvaluationJob.class)); // Once for IN_PROGRESS, once for COMPLETED
        verify(resultRepository, times(1)).save(any(EvaluationResult.class));
    }

    @Test
    void testListen_jobNotFound() {
        when(jobRepository.findById("non-existent-job")).thenReturn(Optional.empty());

        evaluationService.listen("non-existent-job");

        verify(jobRepository, never()).save(any(EvaluationJob.class));
        verify(resultRepository, never()).save(any(EvaluationResult.class));
    }
}
