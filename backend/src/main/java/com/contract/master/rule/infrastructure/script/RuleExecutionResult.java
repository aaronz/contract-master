package com.contract.master.rule.infrastructure.script;

import com.contract.master.rule.domain.model.PageCoordinate;

public class RuleExecutionResult {

    private final boolean matched;
    private final PageCoordinate location;

    public RuleExecutionResult(boolean matched, PageCoordinate location) {
        this.matched = matched;
        this.location = location;
    }

    public boolean isMatched() {
        return matched;
    }

    public PageCoordinate getLocation() {
        return location;
    }
}
