package com.contract.master.shared.domain.model;

import com.contract.master.shared.domain.base.BaseDomainEntity;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Embedded;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Objects;

@MappedSuperclass
@FilterDef(name = "tenantFilter", parameters = @ParamDef(name = "tenantId", type = String.class))
@FilterDef(name = "dataScopeFilter", parameters = @ParamDef(name = "deptIds", type = String.class))
@Filter(name = "tenantFilter", condition = "tenant_id = :tenantId")
@Filter(name = "dataScopeFilter", condition = "owner_dept_id IN (:deptIds)")
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseTenantEntity extends BaseDomainEntity implements TenantAware { 

    protected BaseTenantEntity() {
        
    }
    @Embedded
    private TenantId tenantId;

    @CreatedDate
    @Column(name = "create_time", updatable = false)
    private LocalDateTime createTime;

    @CreatedBy
    @Column(name = "create_user", length = 64, updatable = false)
    private String createUser;

    @LastModifiedDate
    @Column(name = "update_time")
    private LocalDateTime updateTime;

    @LastModifiedBy
    @Column(name = "update_user", length = 64)
    private String updateUser;

    @Override
    public TenantId getTenantId() { return tenantId; }
    @Override
    public void setTenantId(TenantId tenantId) { this.tenantId = tenantId; }

    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
    public String getCreateUser() { return createUser; }
    public void setCreateUser(String createUser) { this.createUser = createUser; }
    public LocalDateTime getUpdateTime() { return updateTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }
    public String getUpdateUser() { return updateUser; }
    public void setUpdateUser(String updateUser) { this.updateUser = updateUser; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        BaseTenantEntity that = (BaseTenantEntity) o;
        return Objects.equals(tenantId, that.tenantId) &&
               Objects.equals(createTime, that.createTime) &&
               Objects.equals(createUser, that.createUser) &&
               Objects.equals(updateTime, that.updateTime) &&
               Objects.equals(updateUser, that.updateUser);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), tenantId, createTime, createUser, updateTime, updateUser);
    }
}
