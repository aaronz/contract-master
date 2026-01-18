package com.contract.master.identity.domain.model;

import com.contract.master.shared.domain.model.BaseTenantEntity;
import jakarta.persistence.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Objects;

@Entity
@Table(name = "role_permission_rel")
@EntityListeners(AuditingEntityListener.class)
public class RolePermissionRel extends BaseTenantEntity {
    // ID is now inherited from BaseEntity
    // No need for explicit ID field here

    @Column(name = "role_id", length = 64)
    private String roleId;

    @Column(name = "perm_id", length = 64)
    private String permId;

    @Column(name = "data_scope", length = 32)
    private String dataScope;

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getPermId() {
        return permId;
    }

    public void setPermId(String permId) {
        this.permId = permId;
    }

    public String getDataScope() {
        return dataScope;
    }

    public void setDataScope(String dataScope) {
        this.dataScope = dataScope;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false; // Compare parent class fields, including inherited ID
        RolePermissionRel that = (RolePermissionRel) o;
        return Objects.equals(roleId, that.roleId) &&
               Objects.equals(permId, that.permId) &&
               Objects.equals(dataScope, that.dataScope);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), roleId, permId, dataScope);
    }
}
