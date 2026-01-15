package com.contract.master.rule;

import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class DroolsRuleEngine {

    public List<String> validate(Object fact, String tenantId) {
        List<String> violations = new ArrayList<>();
        if (fact instanceof com.contract.master.domain.ContractBase) {
            com.contract.master.domain.ContractBase contract = (com.contract.master.domain.ContractBase) fact;
            if (contract.getAmount() != null && contract.getAmount().doubleValue() > 1000000) {
                violations.add("CRITICAL: Large contract requires manual legal review");
            }
        }
        return violations;
    }
}
