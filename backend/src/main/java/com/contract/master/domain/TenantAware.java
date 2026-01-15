package com.contract.master.domain;

public interface TenantAware {
    String getTenantId();
    void setTenantId(String tenantId);
}
