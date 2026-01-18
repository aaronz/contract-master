package com.contract.master.rule;
import com.contract.master.shared.domain.model.TenantId;

import com.contract.master.contract.domain.model.Contract;
import com.contract.master.contract.domain.model.ContractAmount;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class DroolsRuleEngineTest {

    @Autowired
    private DroolsRuleEngine droolsRuleEngine;

    @Test
    void testLargeContractViolation() {
        Contract contract = new Contract();
        contract.setAmount(ContractAmount.of(new BigDecimal("2000000"), "USD"));
        
        List<String> violations = droolsRuleEngine.validate(contract, "tenant-1");
        
        assertFalse(violations.isEmpty());
        assertTrue(violations.get(0).contains("CRITICAL"));
    }

    @Test
    void testSmallContractNoViolation() {
        Contract contract = new Contract();
        contract.setAmount(ContractAmount.of(new BigDecimal("500000"), "USD"));
        
        List<String> violations = droolsRuleEngine.validate(contract, "tenant-1");
        
        assertTrue(violations.isEmpty());
    }
}
