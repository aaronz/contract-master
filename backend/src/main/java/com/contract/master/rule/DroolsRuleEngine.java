package com.contract.master.rule;

import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class DroolsRuleEngine {

    @Autowired
    private KieContainer kieContainer;

    public List<String> validate(Object fact, String tenantId) {
        List<String> violations = new ArrayList<>();
        KieSession kieSession = kieContainer.newKieSession();
        try {
            kieSession.setGlobal("violations", violations);
            kieSession.insert(fact);
            kieSession.fireAllRules();
        } finally {
            kieSession.dispose();
        }
        return violations;
    }
}
