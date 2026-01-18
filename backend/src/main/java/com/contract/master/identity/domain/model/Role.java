package com.contract.master.identity.domain.model;

import com.contract.master.shared.domain.model.BaseTenantEntity;

import jakarta.persistence.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Objects;

@Entity
@Table(name = "role_info")
@EntityListeners(AuditingEntityListener.class)
public class Role extends BaseTenantEntity {
    @Column(name = "role_id", length = 64) // roleId is now a regular field
    private String roleId;

    @Column(name = "role_name", length = 64)
    private String roleName;

    @Column(name = "role_type", length = 32)
    private String roleType;

    @Column(name = "status")
    private Integer status;

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleType() {
        return roleType;
    }

    public void setRoleType(String roleType) {
        this.roleType = roleType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false; // Compare parent class fields, including inherited ID
        Role role = (Role) o;
        return Objects.equals(roleId, role.roleId) &&
               Objects.equals(roleName, role.roleName) &&
               Objects.equals(roleType, role.roleType) &&
               Objects.equals(status, role.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), roleId, roleName, roleType, status);
    }
}
