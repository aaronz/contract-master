package com.contract.master.domain;

import com.contract.master.domain.BaseTenantEntity;
import com.contract.master.domain.TenantEntityListener;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "rule_config")
@Data
@EqualsAndHashCode(callSuper = true)
@EntityListeners({AuditingEntityListener.class, TenantEntityListener.class})
public class RuleConfig extends BaseTenantEntity {
    @Override
    public String getTenantId() { return super.getTenantId(); }
    @Override
    public void setTenantId(String tenantId) { super.setTenantId(tenantId); }

    @Id
    @Column(name = "rule_id", length = 64)
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

    @CreatedDate
    @Column(name = "create_time", updatable = false)
    private LocalDateTime createTime;

    public String getRuleCondition() { return ruleCondition; }
    public void setRuleCondition(String ruleCondition) { this.ruleCondition = ruleCondition; }
    public String getRuleName() { return ruleName; }
    public void setRuleName(String ruleName) { this.ruleName = ruleName; }
    public String getRuleLevel() { return ruleLevel; }
    public void setRuleLevel(String ruleLevel) { this.ruleLevel = ruleLevel; }
    public String getExecutionActions() { return executionActions; }
    public void setExecutionActions(String executionActions) { this.executionActions = executionActions; }
    public Boolean getIsEnabled() { return isEnabled; }
    public void setIsEnabled(Boolean isEnabled) { this.isEnabled = isEnabled; }
    public String getRuleType() { return ruleType; }
    public void setRuleType(String ruleType) { this.ruleType = ruleType; }
    public String getAiPromptTemplate() { return aiPromptTemplate; }
    public void setAiPromptTemplate(String aiPromptTemplate) { this.aiPromptTemplate = aiPromptTemplate; }
    public String getRuleId() { return ruleId; }
    public void setRuleId(String ruleId) { this.ruleId = ruleId; }
    public String getTriggerTime() { return triggerTime; }
    public void setTriggerTime(String triggerTime) { this.triggerTime = triggerTime; }
}
