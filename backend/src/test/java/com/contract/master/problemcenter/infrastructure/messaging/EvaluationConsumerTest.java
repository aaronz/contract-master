package com.contract.master.problemcenter.infrastructure.messaging;

import com.contract.master.contract.application.ContractService;
import com.contract.master.contract.domain.model.Contract;
import com.contract.master.contract.domain.model.ContractId;
import com.contract.master.contract.domain.repository.ContractRepository;
import com.contract.master.contract.dto.ContractDTO;
import com.contract.master.notification.application.NotificationService;
import com.contract.master.problemcenter.domain.model.ProblemEvaluationJob;
import com.contract.master.problemcenter.domain.repository.ProblemEvaluationJobRepository;
import com.contract.master.problemcenter.domain.repository.ProblemRepository;
import com.contract.master.rule.domain.model.Rule;
import com.contract.master.rule.domain.model.RuleLogicType;
import com.contract.master.rule.domain.model.RuleStatus;
import com.contract.master.rule.domain.repository.RuleRepository;
import com.contract.master.rule.infrastructure.script.RuleExecutionResult;
import com.contract.master.rule.infrastructure.script.RuleExecutor;
import com.contract.master.shared.domain.model.TenantId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class EvaluationConsumerTest {

    private EvaluationConsumer consumer;

    @Mock private ProblemEvaluationJobRepository jobRepository;
    @Mock private RuleRepository ruleRepository;
    @Mock private ProblemRepository problemRepository;
    @Mock private ContractRepository contractRepository;
    @Mock private ContractService contractService;
    @Mock private RuleExecutor ruleExecutor;
    @Mock private NotificationService notificationService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        ruleExecutor = mock(GroovyRuleExecutor.class);
        consumer = new EvaluationConsumer(
                jobRepository, ruleRepository, problemRepository,
                contractRepository, contractService, 
                Collections.singletonList(ruleExecutor),
                notificationService
        );
    }

    @Test
    public void testExecuteJob_PopulatesFactsCorrectly() {
        // Arrange
        UUID contractUuid = UUID.randomUUID();
        ProblemEvaluationJob job = new ProblemEvaluationJob();
        ReflectionTestUtils.setField(job, "id", 1L);
        job.setContractId(contractUuid);
        job.setTenantId(TenantId.of("tenant-1"));

        Contract contract = mock(Contract.class);
        when(contract.getContractId()).thenReturn(new ContractId(contractUuid));
        when(contract.getContractName()).thenReturn("Test Contract");
        
        ContractDTO dto = new ContractDTO();
        dto.setContractNo("CON-001");
        dto.setContractName("Test Contract");
        dto.setContractAmount(new BigDecimal("1000.00"));
        dto.setExtendedFields(Map.of("custom_field", "custom_value"));

        when(contractRepository.findById(any())).thenReturn(Optional.of(contract));
        when(contractService.convertToDTO(any())).thenReturn(dto);
        when(jobRepository.findById(1L)).thenReturn(Optional.of(job));

        Rule rule = new Rule();
        ReflectionTestUtils.setField(rule, "id", 1L);
        rule.setName("Test Rule");
        rule.setLogicType(RuleLogicType.GROOVY);
        rule.setStatus(RuleStatus.ACTIVE);
        when(ruleRepository.findByTenantIdAndStatus(any(), any())).thenReturn(Collections.singletonList(rule));

        when(ruleExecutor.execute(any(Rule.class), anyMap())).thenReturn(new RuleExecutionResult(true, null));

        // Act
        consumer.consume("1");

        // Assert
        ArgumentCaptor<Map<String, Object>> factsCaptor = ArgumentCaptor.forClass(Map.class);
        verify(ruleExecutor).execute(eq(rule), factsCaptor.capture());
        
        Map<String, Object> capturedFacts = factsCaptor.getValue();
        assertThat(capturedFacts.get("contractNo")).isEqualTo("CON-001");
        assertThat(capturedFacts.get("contractAmount")).isEqualTo(new BigDecimal("1000.00"));
        assertThat(capturedFacts.get("custom_field")).isEqualTo("custom_value");
        assertThat(capturedFacts.get("content")).isEqualTo("Test Contract");
    }

    private static abstract class GroovyRuleExecutor implements RuleExecutor {}
}
