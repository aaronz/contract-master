package com.contract.master.rule.domain.model;

import com.contract.master.shared.domain.base.BaseDomainEntity;
import com.contract.master.shared.domain.model.TenantId;
import java.util.Objects;

public class Rule extends BaseDomainEntity {
    private final TenantId tenantId;
    private String ruleName;
    private String ruleContent;
    private RuleSeverity severity;
    private boolean isEnabled;

    public Rule(TenantId tenantId, String ruleName) {
        this.tenantId = Objects.requireNonNull(tenantId);
        this.ruleName = Objects.requireNonNull(ruleName);
        this.isEnabled = true;
    }

    public TenantId getTenantId() { return tenantId; }
    public String getRuleName() { return ruleName; }
    public void setRuleName(String ruleName) { this.ruleName = ruleName; }
    public String getRuleContent() { return ruleContent; }
    public void setRuleContent(String ruleContent) { this.ruleContent = ruleContent; }
    public RuleSeverity getSeverity() { return severity; }
    public void setSeverity(RuleSeverity severity) { this.severity = severity; }
    public boolean isEnabled() { return isEnabled; }
    public void setEnabled(boolean enabled) { isEnabled = enabled; }
}
