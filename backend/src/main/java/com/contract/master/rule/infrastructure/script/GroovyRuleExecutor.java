package com.contract.master.rule.infrastructure.script;

import com.contract.master.rule.domain.model.PageCoordinate;
import com.contract.master.rule.domain.model.Rule;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class GroovyRuleExecutor implements RuleExecutor {

    private final CompilerConfiguration config;

    public GroovyRuleExecutor(CompilerConfiguration config) {
        this.config = config;
    }

    @Override
    public RuleExecutionResult execute(Rule rule, Map<String, Object> facts) {
        Binding binding = new Binding();
        facts.forEach(binding::setVariable);
        GroovyShell shell = new GroovyShell(binding, config);
        Object result = shell.evaluate(rule.getLogicContent());

        if (result instanceof Boolean && (Boolean) result) {
            return new RuleExecutionResult(true, PageCoordinate.empty());
        }

        return new RuleExecutionResult(false, PageCoordinate.empty());
    }
}
