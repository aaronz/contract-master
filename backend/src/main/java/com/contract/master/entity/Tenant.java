package com.contract.master.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "tenant_info")
@Data
@EntityListeners(AuditingEntityListener.class)
public class Tenant {
    @Id
    @Column(name = "tenant_id", length = 64)
    private String tenantId;

    @Column(name = "tenant_name", length = 128)
    private String tenantName;

    @Column(name = "contact_person", length = 64)
    private String contactPerson;

    @Column(name = "contact_phone", length = 20)
    private String contactPhone;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status")
    private com.contract.master.constant.TenantStatus status;

    @CreatedDate
    @Column(name = "create_time", updatable = false)
    private LocalDateTime createTime;

    @LastModifiedDate
    @Column(name = "update_time")
    private LocalDateTime updateTime;
}
