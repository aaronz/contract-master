package com.contract.master.evaluation.service;

import com.contract.master.evaluation.model.EvaluationJob;
import com.contract.master.evaluation.model.EvaluationResult;
import com.contract.master.evaluation.repository.EvaluationJobRepository;
import com.contract.master.evaluation.repository.EvaluationResultRepository;
import com.contract.master.service.AuditService;
import com.contract.master.service.ContractService;
import com.contract.master.service.RuleEngineService;
import com.contract.master.dto.ContractDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EvaluationServiceTest {

    @Mock
    private EvaluationJobRepository jobRepository;
    @Mock
    private EvaluationResultRepository resultRepository;
    @Mock
    private KafkaProducerService kafkaProducerService;
    @Mock
    private AuditService auditService;
    @Mock
    private RuleEngineService ruleEngineService;
    @Mock
    private ContractService contractService;
    @Spy
    private ObjectMapper objectMapper = new ObjectMapper();

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
    }

    @Test
    void testCreateAndPublishEvaluationJob() {
        when(jobRepository.save(any(EvaluationJob.class))).thenAnswer(invocation -> {
            EvaluationJob jobArgument = invocation.getArgument(0); // Get the job instance passed to save
            jobArgument.setId("job-123-generated"); // Simulate the repository setting the ID
            return jobArgument; // Return the SAME instance, now with an ID
        });

        EvaluationJob createdJob = evaluationService.createAndPublishEvaluationJob(ruleIds, contractIds, tenantId, triggeredBy);

        assertNotNull(createdJob);
        assertNotNull(createdJob.getId());
        assertEquals("job-123-generated", createdJob.getId());
        assertEquals(EvaluationJob.JobStatus.PENDING, createdJob.getStatus());

        verify(jobRepository, times(1)).save(any(EvaluationJob.class));
        verify(kafkaProducerService, times(1)).sendMessage("job-123-generated");
        
        verify(auditService, times(1)).logChange(
            eq("JOB-job-123-generated"), 
            eq("EvaluationJob"), 
            isNull(), 
            eq("Created"), 
            eq("CREATE"), 
            eq(triggeredBy)
        );
    }

    @Test
    void testListen_jobCompletion() throws Exception {
        // For testListen, the mock should return a job that already has an ID
        EvaluationJob existingJob = new EvaluationJob(tenantId, EvaluationJob.JobStatus.PENDING, EvaluationJob.TriggerType.MANUAL, LocalDateTime.now(), triggeredBy);
        existingJob.setId("job-123");
        existingJob.setTargetRules(objectMapper.writeValueAsString(ruleIds));
        existingJob.setTargetContracts(objectMapper.writeValueAsString(contractIds));

        when(jobRepository.findById(existingJob.getId())).thenReturn(Optional.of(existingJob));
        when(jobRepository.save(any(EvaluationJob.class))).thenReturn(existingJob);
        when(resultRepository.save(any(EvaluationResult.class))).thenReturn(new EvaluationResult());
        when(contractService.getContractById(anyString())).thenReturn(new ContractDTO()); // Return a dummy contract
        when(ruleEngineService.validate(any(ContractDTO.class))).thenReturn(Collections.emptyList()); // Simulate validation passing

        evaluationService.listen(existingJob.getId());

        assertEquals(EvaluationJob.JobStatus.COMPLETED, existingJob.getStatus());
        assertNotNull(existingJob.getCompletedAt());
        verify(jobRepository, times(2)).save(any(EvaluationJob.class)); // Once for IN_PROGRESS, once for COMPLETED
        // Expect resultRepository to be saved twice (once for each contract in contractIds)
        verify(resultRepository, times(2)).save(any(EvaluationResult.class));
    }

    @Test
    void testListen_jobNotFound() {
        when(jobRepository.findById("non-existent-job")).thenReturn(Optional.empty());

        evaluationService.listen("non-existent-job");

        verify(jobRepository, never()).save(any(EvaluationJob.class));
        verify(resultRepository, never()).save(any(EvaluationResult.class));
    }
}
