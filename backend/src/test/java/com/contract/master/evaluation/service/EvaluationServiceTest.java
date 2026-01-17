package com.contract.master.evaluation.service;

import com.contract.master.evaluation.domain.model.EvaluationJob;
import com.contract.master.evaluation.domain.model.EvaluationResult;
import com.contract.master.evaluation.domain.repository.EvaluationJobRepository;
import com.contract.master.evaluation.domain.repository.EvaluationResultRepository;
import com.contract.master.audit.application.AuditService;
import com.contract.master.contract.application.ContractService;
import com.contract.master.evaluation.application.RuleEngineService;
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
import com.contract.master.security.TenantContext; // Added import for TenantContext
import org.mockito.MockedStatic;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.*;
import com.contract.master.evaluation.application.EvaluationApplicationService;
import com.contract.master.evaluation.infrastructure.messaging.KafkaProducerService;

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
    private EvaluationApplicationService evaluationService;

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
            eq("JOB-" + createdJob.getId()), 
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
        when(ruleEngineService.validate(any(ContractDTO.class), eq(ruleIds))).thenReturn(Collections.emptyList()); // Simulate validation passing

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

    @Test
    void testTriggerReEvaluationForSingleContract_Success() throws Exception {
        String singleContractId = "singleContract1";
        List<String> rulesForReEval = Arrays.asList("ruleA", "ruleB");
        String reEvalTriggeredBy = "re-eval-user";
        
        ContractDTO mockContract = new ContractDTO();
        mockContract.setContractId(singleContractId);
        when(contractService.getContractById(singleContractId)).thenReturn(mockContract);

        when(jobRepository.findByTenantIdAndTargetContractsContainingAndStatus(
                eq(tenantId), anyString(), eq(EvaluationJob.JobStatus.IN_PROGRESS)))
                .thenReturn(Collections.emptyList());

        when(jobRepository.save(any(EvaluationJob.class))).thenAnswer(invocation -> {
            EvaluationJob jobArgument = invocation.getArgument(0);
            jobArgument.setId("job-re-eval-123");
            return jobArgument;
        });

        // Mock TenantContext
        try (MockedStatic<TenantContext> mocked = mockStatic(TenantContext.class)) {
            mocked.when(TenantContext::getCurrentTenant).thenReturn(tenantId);

            String jobId = evaluationService.triggerReEvaluationForSingleContract(singleContractId, rulesForReEval, reEvalTriggeredBy);

            assertNotNull(jobId);
            assertEquals("job-re-eval-123", jobId);
            verify(contractService, times(1)).getContractById(singleContractId);
            verify(jobRepository, times(1)).findByTenantIdAndTargetContractsContainingAndStatus(
                    eq(tenantId), anyString(), eq(EvaluationJob.JobStatus.IN_PROGRESS));
            verify(jobRepository, times(1)).save(any(EvaluationJob.class));
            verify(auditService, times(1)).logReEvaluationTriggered(singleContractId, String.join(",", rulesForReEval), reEvalTriggeredBy);
            verify(kafkaProducerService, times(1)).sendMessage("job-re-eval-123");
        }
    }

    @Test
    void testTriggerReEvaluationForSingleContract_ContractNotFound() {
        String singleContractId = "nonExistentContract";
        List<String> rulesForReEval = Arrays.asList("ruleA");
        String reEvalTriggeredBy = "re-eval-user";

        when(contractService.getContractById(singleContractId)).thenThrow(new IllegalArgumentException("Contract not found"));

        try (MockedStatic<TenantContext> mocked = mockStatic(TenantContext.class)) {
            mocked.when(TenantContext::getCurrentTenant).thenReturn(tenantId);
            
            assertThrows(IllegalArgumentException.class, () ->
                    evaluationService.triggerReEvaluationForSingleContract(singleContractId, rulesForReEval, reEvalTriggeredBy));

            verify(contractService, times(1)).getContractById(singleContractId);
            verify(jobRepository, never()).findByTenantIdAndTargetContractsContainingAndStatus(anyString(), anyString(), any());
            verify(jobRepository, never()).save(any(EvaluationJob.class));
            verify(auditService, never()).logReEvaluationTriggered(anyString(), anyString(), anyString());
            verify(kafkaProducerService, never()).sendMessage(anyString());
        }
    }

    @Test
    void testTriggerReEvaluationForSingleContract_AlreadyInProgress() throws Exception {
        String singleContractId = "contractInProg";
        List<String> rulesForReEval = Arrays.asList("ruleA");
        String reEvalTriggeredBy = "re-eval-user";
        
        ContractDTO mockContract = new ContractDTO();
        mockContract.setContractId(singleContractId);
        when(contractService.getContractById(singleContractId)).thenReturn(mockContract);

        when(jobRepository.findByTenantIdAndTargetContractsContainingAndStatus(
                eq(tenantId), anyString(), eq(EvaluationJob.JobStatus.IN_PROGRESS)))
                .thenReturn(Arrays.asList(new EvaluationJob())); // Simulate job in progress

        try (MockedStatic<TenantContext> mocked = mockStatic(TenantContext.class)) {
            mocked.when(TenantContext::getCurrentTenant).thenReturn(tenantId);
            
            assertThrows(IllegalStateException.class, () ->
                    evaluationService.triggerReEvaluationForSingleContract(singleContractId, rulesForReEval, reEvalTriggeredBy));

            verify(contractService, times(1)).getContractById(singleContractId);
            verify(jobRepository, times(1)).findByTenantIdAndTargetContractsContainingAndStatus(
                    eq(tenantId), anyString(), eq(EvaluationJob.JobStatus.IN_PROGRESS));
            verify(jobRepository, never()).save(any(EvaluationJob.class));
            verify(auditService, never()).logReEvaluationTriggered(anyString(), anyString(), anyString());
            verify(kafkaProducerService, never()).sendMessage(anyString());
        }
    }
}