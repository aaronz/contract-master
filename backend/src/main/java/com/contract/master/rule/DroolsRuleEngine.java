package com.contract.master.rule;

import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class DroolsRuleEngine {

    public List<String> validate(Object fact, String tenantId) {
        List<String> violations = new ArrayList<>();
        return violations;
    }
}
