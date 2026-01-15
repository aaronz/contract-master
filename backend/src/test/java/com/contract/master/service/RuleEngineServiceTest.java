package com.contract.master.service;

import com.contract.master.domain.RuleConfig;
import com.contract.master.domain.RuleConfigRepository;
import com.contract.master.dto.ContractDTO;
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
public class RuleEngineServiceTest {

    @Mock
    private RuleConfigRepository ruleConfigRepository;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private RuleEngineService ruleEngineService;

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

        when(ruleConfigRepository.findByTenantIdAndIsEnabled("tenant-1", true))
                .thenReturn(Collections.singletonList(rule));

        ContractDTO contract = new ContractDTO();
        contract.setContractNo("CON-TEST");
        contract.setContractAmount(new BigDecimal("20000.00"));

        List<String> violations = ruleEngineService.validate(contract);

        assertFalse(violations.isEmpty());
        assertTrue(violations.get(0).contains("High Amount Rule"));
        verify(notificationService).sendNotification(eq("admin"), anyString(), anyString(), eq("RISK"));
    }

    @Test
    void testValidateWithoutViolation() {
        RuleConfig rule = new RuleConfig();
        rule.setRuleName("High Amount Rule");
        rule.setRuleCondition("contractAmount > 10000");
        rule.setIsEnabled(true);

        when(ruleConfigRepository.findByTenantIdAndIsEnabled("tenant-1", true))
                .thenReturn(Collections.singletonList(rule));

        ContractDTO contract = new ContractDTO();
        contract.setContractAmount(new BigDecimal("5000.00"));

        List<String> violations = ruleEngineService.validate(contract);

        assertTrue(violations.isEmpty());
        verify(notificationService, never()).sendNotification(anyString(), anyString(), anyString(), anyString());
    }

    @Test
    void testValidateWithInvalidExpression() {
        RuleConfig rule = new RuleConfig();
        rule.setRuleCondition("invalid_property == true");
        rule.setIsEnabled(true);

        when(ruleConfigRepository.findByTenantIdAndIsEnabled("tenant-1", true))
                .thenReturn(Collections.singletonList(rule));

        ContractDTO contract = new ContractDTO();
        
        List<String> violations = ruleEngineService.validate(contract);
        
        assertTrue(violations.isEmpty());
    }
}
