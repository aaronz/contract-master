package com.contract.master.contract.domain.event;

import java.util.UUID;

public class ContractSavedEvent {
    private final UUID contractId;
    private final String tenantId;

    public ContractSavedEvent(UUID contractId, String tenantId) {
        this.contractId = contractId;
        this.tenantId = tenantId;
    }

    public UUID getContractId() { return contractId; }
    public String getTenantId() { return tenantId; }
}
