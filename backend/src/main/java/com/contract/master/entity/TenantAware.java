package com.contract.master.entity;

public interface TenantAware {
    String getTenantId();
    void setTenantId(String tenantId);
}
