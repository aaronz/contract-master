package com.contract.master.rule.domain.model;

import com.contract.master.shared.domain.model.BaseTenantEntity;
import com.contract.master.shared.infrastructure.persistence.TenantEntityListener;
import jakarta.persistence.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Objects;

@Entity
@Table(name = "rule_config")
@EntityListeners({AuditingEntityListener.class, TenantEntityListener.class})
public class RuleConfig extends BaseTenantEntity {

    @Column(name = "rule_id", length = 64) // ruleId is now a business key, not the primary key
    private String ruleId;

    @Column(name = "rule_name", length = 128)
    private String ruleName;

    @Column(name = "rule_type", length = 32)
    private String ruleType;

    @Column(name = "ai_prompt_template", columnDefinition = "TEXT")
    private String aiPromptTemplate;

    @Column(name = "rule_level", length = 32)
    private String ruleLevel;

    @Column(name = "trigger_time", length = 32)
    private String triggerTime;

    @Column(name = "rule_condition", columnDefinition = "TEXT")
    private String ruleCondition;

    @Column(name = "action_config", columnDefinition = "TEXT")
    private String actionConfig;

    @Column(name = "execution_actions", columnDefinition = "TEXT")
    private String executionActions;

    @Column(name = "is_enabled")
    private Boolean isEnabled;

    // Getters and setters for inherited ID should use super.getId()/setId()
    // For other fields, keep existing getters/setters

    public String getRuleId() {
        return ruleId;
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public String getRuleType() {
        return ruleType;
    }

    public void setRuleType(String ruleType) {
        this.ruleType = ruleType;
    }

    public String getAiPromptTemplate() {
        return aiPromptTemplate;
    }

    public void setAiPromptTemplate(String aiPromptTemplate) {
        this.aiPromptTemplate = aiPromptTemplate;
    }

    public String getRuleLevel() {
        return ruleLevel;
    }

    public void setRuleLevel(String ruleLevel) {
        this.ruleLevel = ruleLevel;
    }

    public String getTriggerTime() {
        return triggerTime;
    }

    public void setTriggerTime(String triggerTime) {
        this.triggerTime = triggerTime;
    }

    public String getRuleCondition() {
        return ruleCondition;
    }

    public void setRuleCondition(String ruleCondition) {
        this.ruleCondition = ruleCondition;
    }

    public String getActionConfig() {
        return actionConfig;
    }

    public void setActionConfig(String actionConfig) {
        this.actionConfig = actionConfig;
    }

    public String getExecutionActions() {
        return executionActions;
    }

    public void setExecutionActions(String executionActions) {
        this.executionActions = executionActions;
    }

    public Boolean getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(Boolean enabled) {
        isEnabled = enabled;
    }
    
    // equals and hashCode should be updated to consider the inherited ID and its own fields
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false; // Compare parent class fields, including inherited ID
        RuleConfig that = (RuleConfig) o;
        return Objects.equals(ruleId, that.ruleId) &&
               Objects.equals(ruleName, that.ruleName) &&
               Objects.equals(ruleType, that.ruleType) &&
               Objects.equals(aiPromptTemplate, that.aiPromptTemplate) &&
               Objects.equals(ruleLevel, that.ruleLevel) &&
               Objects.equals(triggerTime, that.triggerTime) &&
               Objects.equals(ruleCondition, that.ruleCondition) &&
               Objects.equals(actionConfig, that.actionConfig) &&
               Objects.equals(executionActions, that.executionActions) &&
               Objects.equals(isEnabled, that.isEnabled);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), ruleId, ruleName, ruleType, aiPromptTemplate, ruleLevel, triggerTime, ruleCondition, actionConfig, executionActions, isEnabled);
    }
}