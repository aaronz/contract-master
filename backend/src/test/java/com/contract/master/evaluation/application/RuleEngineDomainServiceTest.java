package com.contract.master.evaluation.application;

import com.contract.master.shared.domain.model.TenantId;
import com.contract.master.rule.domain.model.RuleConfig;
import com.contract.master.evaluation.domain.repository.RuleConfigRepository;
import com.contract.master.notification.application.NotificationService;
import com.contract.master.ai.application.AIService;
import com.contract.master.contract.dto.ContractDTO;
import com.contract.master.security.TenantContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RuleEngineDomainServiceTest {

    @Mock
    private RuleConfigRepository ruleConfigRepository;

    @Mock
    private NotificationService notificationService;

    @Mock
    private AIService aiService;

    @InjectMocks
    private com.contract.master.evaluation.domain.service.RuleEngineDomainService ruleEngineDomainService;

    @BeforeEach
    void setUp() {
        TenantContext.setCurrentTenant("tenant-1");
    }

    @AfterEach
    void tearDown() {
        TenantContext.clear();
    }

    @Test
    void testValidateWithViolation() {
        RuleConfig rule = new RuleConfig();
        rule.setRuleName("High Amount Rule");
        rule.setRuleCondition("contractAmount > 10000");
        rule.setRuleLevel("HIGH");
        rule.setExecutionActions("NOTIFY");
        rule.setIsEnabled(true);

        lenient().when(ruleConfigRepository.findByTenantId(eq("tenant-1")))
                .thenReturn(Collections.singletonList(rule));


        ContractDTO contract = new ContractDTO();
        contract.setContractAmount(new BigDecimal("5000.00"));

        List<String> violations = ruleEngineDomainService.validate(contract, null);

        assertTrue(violations.isEmpty());
        verify(notificationService, never()).sendNotification(anyString(), anyString(), anyString(), anyString());
    }

    @Test
    void testValidateWithInvalidExpression() {
        RuleConfig rule = new RuleConfig();
        rule.setRuleCondition("invalid_property == true");
        rule.setIsEnabled(true);

        lenient().when(ruleConfigRepository.findByTenantId(eq("tenant-1")))
                .thenReturn(Collections.singletonList(rule));

        ContractDTO contract = new ContractDTO();
        
        List<String> violations = ruleEngineDomainService.validate(contract, null);
        
        assertTrue(violations.isEmpty());
    }
}
