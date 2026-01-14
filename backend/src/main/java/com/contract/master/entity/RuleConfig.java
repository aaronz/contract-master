package com.contract.master.entity;

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
    @Id
    @Column(name = "rule_id", length = 64)
    private String ruleId;

    @Column(name = "rule_name", length = 128)
    private String ruleName;

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
}
