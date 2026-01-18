package com.contract.master.shared.domain.model;

public interface TenantAware {
    TenantId getTenantId();
    void setTenantId(TenantId tenantId);
}
