package com.contract.master.contract.metadata.domain.event;

public class FieldConfigChangedEvent {
    private final String tenantId;

    public FieldConfigChangedEvent(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getTenantId() {
        return tenantId;
    }
}
