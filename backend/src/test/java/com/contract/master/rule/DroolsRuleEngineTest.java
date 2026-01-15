package com.contract.master.rule;

import com.contract.master.domain.ContractBase;
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
        ContractBase contract = new ContractBase();
        contract.setAmount(new BigDecimal("2000000"));
        
        List<String> violations = droolsRuleEngine.validate(contract, "tenant-1");
        
        assertFalse(violations.isEmpty());
        assertTrue(violations.get(0).contains("CRITICAL"));
    }

    @Test
    void testSmallContractNoViolation() {
        ContractBase contract = new ContractBase();
        contract.setAmount(new BigDecimal("500000"));
        
        List<String> violations = droolsRuleEngine.validate(contract, "tenant-1");
        
        assertTrue(violations.isEmpty());
    }
}
