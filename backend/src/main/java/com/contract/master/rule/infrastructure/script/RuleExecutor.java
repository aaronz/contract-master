package com.contract.master.rule.infrastructure.script;

import com.contract.master.rule.domain.model.PageCoordinate;
import com.contract.master.rule.domain.model.Rule;
import java.util.Map;

public interface RuleExecutor {
    RuleExecutionResult execute(Rule rule, Map<String, Object> facts);
}
