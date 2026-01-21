package com.contract.master.integration.domain.model;

import java.util.UUID;

public class IntegrationEvent {
    private UUID eventId;
    private String contractId;
    private String systemId;
    private String tenantId;
    private Integer attempt;
    private Long timestamp;

    public IntegrationEvent() {
    }

    public IntegrationEvent(String contractId, String systemId, String tenantId) {
        this.eventId = UUID.randomUUID();
        this.contractId = contractId;
        this.systemId = systemId;
        this.tenantId = tenantId;
        this.attempt = 1;
        this.timestamp = System.currentTimeMillis();
    }

    public UUID getEventId() { return eventId; }
    public void setEventId(UUID eventId) { this.eventId = eventId; }
    public String getContractId() { return contractId; }
    public void setContractId(String contractId) { this.contractId = contractId; }
    public String getSystemId() { return systemId; }
    public void setSystemId(String systemId) { this.systemId = systemId; }
    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    public Integer getAttempt() { return attempt; }
    public void setAttempt(Integer attempt) { this.attempt = attempt; }
    public Long getTimestamp() { return timestamp; }
    public void setTimestamp(Long timestamp) { this.timestamp = timestamp; }
}
