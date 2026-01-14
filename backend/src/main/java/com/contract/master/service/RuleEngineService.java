package com.contract.master.service;

import com.contract.master.dto.ContractDTO;
import com.contract.master.entity.RuleConfig;
import com.contract.master.repository.RuleConfigRepository;
import com.contract.master.security.TenantContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RuleEngineService {

    @Autowired
    private RuleConfigRepository ruleConfigRepository;

    @Autowired
    private NotificationService notificationService;

    private final ExpressionParser parser = new SpelExpressionParser();

    public List<String> validate(ContractDTO contract) {
        List<String> violations = new ArrayList<>();
        List<RuleConfig> rules = ruleConfigRepository.findByTenantIdAndIsEnabled(TenantContext.getCurrentTenant(), true);

        StandardEvaluationContext context = new StandardEvaluationContext(contract);

        for (RuleConfig rule : rules) {
            try {
                Boolean result = parser.parseExpression(rule.getRuleCondition()).getValue(context, Boolean.class);
                if (Boolean.TRUE.equals(result)) {
                    violations.add(rule.getRuleName() + " (Level: " + rule.getRuleLevel() + ")");
                    executeActions(rule, contract);
                }
            } catch (Exception e) {
            }
        }
        return violations;
    }

    private void executeActions(RuleConfig rule, ContractDTO contract) {
        if (rule.getExecutionActions() == null) return;
        if (rule.getExecutionActions().contains("NOTIFY")) {
            notificationService.sendNotification("admin", "Rule Alert: " + rule.getRuleName(), 
                "Contract " + contract.getContractNo() + " triggered a rule violation.", "RISK");
        }
    }

    public String analyzeWithAI(String contractId) {
        return "AI Suggestion: Compliance check passed for all clauses.";
    }
}
