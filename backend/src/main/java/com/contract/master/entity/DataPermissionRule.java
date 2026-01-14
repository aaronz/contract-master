package com.contract.master.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "data_permission_rule")
@Data
@EqualsAndHashCode(callSuper = true)
@EntityListeners({TenantEntityListener.class})
public class DataPermissionRule extends BaseTenantEntity {
    @Id
    @Column(name = "rule_id", length = 64)
    private String ruleId;

    @Column(name = "role_id", length = 64)
    private String roleId;

    @Column(name = "rule_name", length = 128)
    private String ruleName;

    @Column(name = "data_type", length = 64)
    private String dataType;

    @Column(name = "filter_condition", columnDefinition = "TEXT")
    private String filterCondition;

    @Column(name = "is_enabled")
    private Boolean isEnabled;
}
