package com.contract.master.identity.domain.model;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Objects;

import com.contract.master.shared.domain.base.BaseDomainEntity;

@Entity
@Table(name = "permission_info")
@EntityListeners(AuditingEntityListener.class)
public class Permission extends BaseDomainEntity {
    @Column(name = "perm_id", length = 64)
    private String permId;

    @Column(name = "perm_code", length = 64, unique = true)
    private String permCode;

    @Column(name = "perm_name", length = 64)
    private String permName;

    @Column(name = "perm_type", length = 32)
    private String permType;

    @CreatedDate
    @Column(name = "create_time", updatable = false)
    private LocalDateTime createTime;

    public String getPermId() {
        return permId;
    }

    public void setPermId(String permId) {
        this.permId = permId;
    }

    public String getPermCode() {
        return permCode;
    }

    public void setPermCode(String permCode) {
        this.permCode = permCode;
    }

    public String getPermName() {
        return permName;
    }

    public void setPermName(String permName) {
        this.permName = permName;
    }

    public String getPermType() {
        return permType;
    }

    public void setPermType(String permType) {
        this.permType = permType;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Permission that = (Permission) o;
        return Objects.equals(permId, that.permId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(permId);
    }
}
