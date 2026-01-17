package com.contract.master.shared.domain.model;

public interface TenantAware {
    String getTenantId();
    void setTenantId(String tenantId);
}
