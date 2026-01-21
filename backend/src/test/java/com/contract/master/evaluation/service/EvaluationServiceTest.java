package com.contract.master.evaluation.service;
import com.contract.master.shared.domain.model.TenantId;

import com.contract.master.evaluation.domain.model.EvaluationJob;
import com.contract.master.evaluation.domain.model.EvaluationResult;
import com.contract.master.evaluation.domain.model.JobStatus;
import com.contract.master.evaluation.domain.repository.EvaluationJobRepository;
import com.contract.master.evaluation.domain.repository.EvaluationResultRepository;
import com.contract.master.audit.application.AuditService;
import com.contract.master.contract.application.ContractService;
import com.contract.master.evaluation.domain.service.RuleEngineDomainService;
import com.contract.master.contract.dto.ContractDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Collections;
import com.contract.master.security.TenantContext;
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
    private RuleEngineDomainService ruleEngineDomainService;
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

        testJob = new EvaluationJob(tenantId, JobStatus.PENDING, EvaluationJob.TriggerType.MANUAL, LocalDateTime.now(), triggeredBy);
        testJob.setJobId("job-123");
    }

    @Test
    void testCreateAndPublishEvaluationJob() {
        when(jobRepository.save(any(EvaluationJob.class))).thenAnswer(invocation -> invocation.getArgument(0));

        EvaluationJob createdJob = evaluationService.createAndPublishEvaluationJob(ruleIds, contractIds, tenantId, triggeredBy);

        assertNotNull(createdJob);
        assertNotNull(createdJob.getJobId());
        assertEquals(JobStatus.PENDING, createdJob.getStatus());

        verify(jobRepository, times(1)).save(any(EvaluationJob.class));
        verify(kafkaProducerService, times(1)).sendMessage(anyString());
    }

    @Test
    void testListen_jobCompletion() throws Exception {
        EvaluationJob existingJob = new EvaluationJob(tenantId, JobStatus.PENDING, EvaluationJob.TriggerType.MANUAL, LocalDateTime.now(), triggeredBy);
        existingJob.setJobId("job-123");
        existingJob.setTargetRules(objectMapper.writeValueAsString(ruleIds));
        existingJob.setTargetContracts(objectMapper.writeValueAsString(contractIds));

        when(jobRepository.findByJobId(existingJob.getJobId())).thenReturn(Optional.of(existingJob));
        when(jobRepository.save(any(EvaluationJob.class))).thenReturn(existingJob);
        when(resultRepository.save(any(EvaluationResult.class))).thenReturn(new EvaluationResult());
        when(contractService.getContractById(anyString())).thenReturn(new ContractDTO());
        when(ruleEngineDomainService.validate(any(ContractDTO.class), eq(ruleIds))).thenReturn(Collections.emptyList());

        evaluationService.listen(existingJob.getJobId());

        assertEquals(JobStatus.COMPLETED, existingJob.getStatus());
        assertNotNull(existingJob.getCompletedAt());
        verify(jobRepository, times(2)).save(any(EvaluationJob.class));
        verify(resultRepository, times(2)).save(any(EvaluationResult.class));
    }

    @Test
    void testListen_jobNotFound() {
        when(jobRepository.findByJobId("non-existent-job")).thenReturn(Optional.empty());

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

        when(jobRepository.findByTargetContractsContainingAndStatus(
                anyString(), eq(JobStatus.IN_PROGRESS)))
                .thenReturn(Collections.emptyList());

        when(jobRepository.save(any(EvaluationJob.class))).thenAnswer(invocation -> invocation.getArgument(0));

        try (MockedStatic<TenantContext> mocked = mockStatic(TenantContext.class)) {
            mocked.when(TenantContext::getCurrentTenant).thenReturn(tenantId);

            String jobId = evaluationService.triggerReEvaluationForSingleContract(singleContractId, rulesForReEval, reEvalTriggeredBy);

            assertNotNull(jobId);
            verify(contractService, times(1)).getContractById(singleContractId);
            verify(jobRepository, times(1)).findByTargetContractsContainingAndStatus(
                    anyString(), eq(JobStatus.IN_PROGRESS));
            verify(jobRepository, times(1)).save(any(EvaluationJob.class));
            verify(auditService, times(1)).logReEvaluationTriggered(singleContractId, String.join(",", rulesForReEval), reEvalTriggeredBy);
            verify(kafkaProducerService, times(1)).sendMessage(anyString());
        }
    }
}
