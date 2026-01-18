package com.contract.master.rule.infrastructure.script;

import com.contract.master.rule.domain.model.PageCoordinate;
import com.contract.master.rule.domain.model.Rule;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.regex.Pattern;

@Component
public class RegexRuleExecutor implements RuleExecutor {

    @Override
    public RuleExecutionResult execute(Rule rule, Map<String, Object> facts) {
        String content = (String) facts.get("content");
        if (content == null) return new RuleExecutionResult(false, PageCoordinate.empty());
        boolean matched = Pattern.compile(rule.getLogicContent()).matcher(content).find();
        return new RuleExecutionResult(matched, PageCoordinate.empty());
    }
}
