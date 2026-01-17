package com.contract.master.evaluation.application;

import com.contract.master.evaluation.domain.model.RuleConfig;
import com.contract.master.evaluation.domain.repository.RuleConfigRepository;
import com.contract.master.notification.application.NotificationService;
import com.contract.master.ai.application.AIService;
import com.contract.master.contract.application.ContractService;
import com.contract.master.dto.ContractDTO;
import com.contract.master.security.TenantContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RuleEngineService {

    @Autowired
    private RuleConfigRepository ruleConfigRepository;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private AIService aiService;

    @Autowired
    private ContractService contractService;

    @org.springframework.transaction.annotation.Transactional
    public List<RuleConfig> batchUpdate(List<RuleConfig> rules) {
        return ruleConfigRepository.saveAll(rules);
    }

    public List<String> validate(ContractDTO contract) {
        return validate(contract, null);
    }

    public List<String> validate(ContractDTO contract, List<String> specificRuleIds) {
        String tenantId = TenantContext.getCurrentTenant();
        List<RuleConfig> rules = ruleConfigRepository.findByTenantIdAndIsEnabled(tenantId, true);
        
        if (specificRuleIds != null && !specificRuleIds.isEmpty()) {
            rules = rules.stream()
                    .filter(r -> specificRuleIds.contains(r.getRuleId()))
                    .collect(Collectors.toList());
        }

        List<String> violations = new ArrayList<>();

        for (RuleConfig rule : rules) {
            if ("AI_PROMPT".equals(rule.getRuleType())) {
                String aiResult = aiService.analyzeWithPrompt(contract, rule.getAiPromptTemplate());
                if (aiResult != null && aiResult.contains("VIOLATION")) {
                    violations.add("AI Rule [" + rule.getRuleName() + "]: " + aiResult);
                }
            } else if (rule.getRuleCondition() != null) {
                try {
                    ExpressionParser parser = new SpelExpressionParser();
                    Expression exp = parser.parseExpression(rule.getRuleCondition());
                    StandardEvaluationContext context = new StandardEvaluationContext(contract);
                    Boolean result = exp.getValue(context, Boolean.class);
                    if (Boolean.TRUE.equals(result)) {
                        violations.add("Business Rule [" + rule.getRuleName() + "]: " + rule.getRuleName() + " violated");
                        if ("NOTIFY".equals(rule.getExecutionActions())) {
                            notificationService.sendNotification("admin", "Rule Violation", 
                                "Contract " + (contract != null ? contract.getContractNo() : "N/A") + " violated rule: " + rule.getRuleName(), "RISK");
                        }
                    }
                } catch (Exception e) {}
            }
        }
        return violations;
    }

    public String analyzeWithAI(String contractId) {
        ContractDTO contract = contractService.getContractById(contractId);
        if (contract == null) return "Contract not found";
        
        List<String> violations = validate(contract);
        return violations.stream()
                .filter(v -> v.startsWith("AI Rule"))
                .collect(Collectors.joining("; "));
    }
}
