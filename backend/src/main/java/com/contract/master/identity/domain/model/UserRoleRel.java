package com.contract.master.identity.domain.model;

import com.contract.master.shared.domain.model.BaseTenantEntity;
import jakarta.persistence.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Objects;

@Entity
@Table(name = "user_role_rel")
@EntityListeners(AuditingEntityListener.class)
public class UserRoleRel extends BaseTenantEntity {
    // ID is now inherited from BaseEntity
    // No need for explicit ID field here

    @Column(name = "user_id", length = 64)
    private String userId;

    @Column(name = "role_id", length = 64)
    private String roleId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false; // Compare parent class fields, including inherited ID
        UserRoleRel that = (UserRoleRel) o;
        return Objects.equals(userId, that.userId) &&
               Objects.equals(roleId, that.roleId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), userId, roleId);
    }
}
