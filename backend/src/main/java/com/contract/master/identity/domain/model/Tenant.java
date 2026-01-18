package com.contract.master.identity.domain.model;

import com.contract.master.shared.domain.base.AggregateRoot;
import com.contract.master.shared.domain.base.BaseDomainEntity;
import com.contract.master.shared.domain.model.TenantId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import java.util.Objects;

@Entity
@Table(name = "tenant_info")
public class Tenant extends BaseDomainEntity implements AggregateRoot {
    @Column(name = "tenant_id", length = 64)
    private String tenantId;

    @Column(name = "tenant_name", length = 128)
    private String name;

    @Column(name = "status", length = 32)
    private String status;

    public Tenant() {}

    public Tenant(String tenantId, String name) {
        this.tenantId = tenantId;
        this.name = Objects.requireNonNull(name);
        this.status = "ACTIVE";
    }

    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
