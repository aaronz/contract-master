package com.contract.master.rule.infrastructure.script;

import com.contract.master.rule.domain.model.PageCoordinate;
import com.contract.master.rule.domain.model.Rule;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class LogicRuleExecutor implements RuleExecutor {

    private static final Logger log = LoggerFactory.getLogger(LogicRuleExecutor.class);
    private final ObjectMapper objectMapper;
    private final ExpressionParser parser = new SpelExpressionParser();

    public LogicRuleExecutor(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public RuleExecutionResult execute(Rule rule, Map<String, Object> facts) {
        try {
            String logicContent = rule.getLogicContent();
            if (logicContent == null || logicContent.isEmpty()) {
                return new RuleExecutionResult(false, PageCoordinate.empty());
            }

            JsonNode rootNode = objectMapper.readTree(logicContent);
            String spelExpression = convertToSpel(rootNode);
            
            if (spelExpression.isEmpty()) {
                log.warn("Empty SpEL expression generated for rule: {}", rule.getName());
                return new RuleExecutionResult(false, PageCoordinate.empty());
            }

            log.debug("Executing SpEL: {} for rule: {}", spelExpression, rule.getName());

            Expression exp = parser.parseExpression(spelExpression);
            StandardEvaluationContext context = new StandardEvaluationContext();
            context.setVariables(facts);
            if (facts.containsKey("contract")) {
                context.setRootObject(facts.get("contract"));
            }

            Boolean matched = exp.getValue(context, Boolean.class);
            log.debug("Rule: {} result: {}", rule.getName(), matched);
            return new RuleExecutionResult(Boolean.TRUE.equals(matched), PageCoordinate.empty());
        } catch (Exception e) {
            log.error("Failed to execute logic rule: {}", rule.getName(), e);
            return new RuleExecutionResult(false, PageCoordinate.empty());
        }
    }

    private String convertToSpel(JsonNode node) {
        if (node.has("type") && "group".equals(node.get("type").asText())) {
            String operator = node.has("operator") ? node.get("operator").asText() : "AND";
            String spelOp = "AND".equalsIgnoreCase(operator) ? " && " : " || ";
            
            List<String> childrenSpel = new ArrayList<>();
            if (node.has("children") && node.get("children").isArray()) {
                for (JsonNode child : node.get("children")) {
                    String childSpel = convertToSpel(child);
                    if (!childSpel.isEmpty()) {
                        childrenSpel.add(childSpel);
                    }
                }
            }
            
            if (childrenSpel.isEmpty()) return "";
            return "(" + String.join(spelOp, childrenSpel) + ")";
        } else if (node.has("type") && "rule".equals(node.get("type").asText())) {
            String field = node.has("field") ? node.get("field").asText() : "";
            String operator = node.has("operator") ? node.get("operator").asText() : "eq";
            String value = node.has("value") ? node.get("value").asText() : "";

            if (field.isEmpty()) return "";

            switch (operator) {
                case "gt": return field + " > " + value;
                case "lt": return field + " < " + value;
                case "eq": return field + " == '" + value + "'";
                case "neq": return field + " != '" + value + "'";
                case "contains": return field + ".contains('" + value + "')";
                case "empty": return field + " == null || " + field + ".isEmpty()";
                default: return field + " == '" + value + "'";
            }
        }
        return "";
    }
}
