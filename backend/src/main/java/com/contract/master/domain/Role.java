package com.contract.master.domain;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "role_info")
@Data
@EntityListeners(AuditingEntityListener.class)
public class Role extends BaseTenantEntity {
    @Id
    @Column(name = "role_id", length = 64)
    private String roleId;

    @Column(name = "role_name", length = 64)
    private String roleName;

    @Column(name = "role_type", length = 32)
    private String roleType;

    @Column(name = "status")
    private Integer status;

    @CreatedDate
    @Column(name = "create_time", updatable = false)
    private LocalDateTime createTime;

    @LastModifiedDate
    @Column(name = "update_time")
    private LocalDateTime updateTime;

    @Override
    public String getTenantId() { return super.getTenantId(); }
    @Override
    public void setTenantId(String tenantId) { super.setTenantId(tenantId); }
}
