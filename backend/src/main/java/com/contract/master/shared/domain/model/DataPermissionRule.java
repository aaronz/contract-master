package com.contract.master.shared.domain.model;

import jakarta.persistence.*;
import com.contract.master.shared.infrastructure.persistence.TenantEntityListener;

import java.util.Objects;

@Entity
@Table(name = "data_permission_rule")
@EntityListeners({TenantEntityListener.class})
public class DataPermissionRule extends BaseTenantEntity {
    @Column(name = "rule_id", length = 64) // ruleId is now a regular field
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

    public String getRuleId() {
        return ruleId;
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getFilterCondition() {
        return filterCondition;
    }

    public void setFilterCondition(String filterCondition) {
        this.filterCondition = filterCondition;
    }

    public Boolean getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(Boolean enabled) {
        isEnabled = enabled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        DataPermissionRule that = (DataPermissionRule) o;
        return Objects.equals(ruleId, that.ruleId) &&
               Objects.equals(roleId, that.roleId) &&
               Objects.equals(ruleName, that.ruleName) &&
               Objects.equals(dataType, that.dataType) &&
               Objects.equals(filterCondition, that.filterCondition) &&
               Objects.equals(isEnabled, that.isEnabled);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), ruleId, roleId, ruleName, dataType, filterCondition, isEnabled);
    }
}
