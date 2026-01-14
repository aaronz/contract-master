package com.contract.master.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@Data
@FilterDefs({
    @FilterDef(name = "tenantFilter", parameters = @ParamDef(name = "tenantId", type = String.class)),
    @FilterDef(name = "dataScopeFilter", parameters = @ParamDef(name = "deptIds", type = String.class))
})
@Filters({
    @Filter(name = "tenantFilter", condition = "tenant_id = :tenantId"),
    @Filter(name = "dataScopeFilter", condition = "owner_dept_id IN (:deptIds)")
})
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseTenantEntity implements TenantAware {
    @Column(name = "tenant_id", length = 64)
    private String tenantId;

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
}
