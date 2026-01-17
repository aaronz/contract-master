package com.contract.master.identity.domain.model;

import com.contract.master.shared.domain.model.BaseTenantEntity;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "role_permission_rel")
@Data
@EntityListeners(AuditingEntityListener.class)
public class RolePermissionRel extends BaseTenantEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "role_id", length = 64)
    private String roleId;

    @Column(name = "perm_id", length = 64)
    private String permId;

    @Column(name = "data_scope", length = 32)
    private String dataScope;

    @CreatedDate
    @Column(name = "create_time", updatable = false)
    private LocalDateTime createTime;

    @Override
    public String getTenantId() { return super.getTenantId(); }
    @Override
    public void setTenantId(String tenantId) { super.setTenantId(tenantId); }
}
