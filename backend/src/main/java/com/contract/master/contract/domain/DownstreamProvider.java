package com.contract.master.contract.domain;

import com.contract.master.shared.domain.model.TenantId;
import java.util.List;

/**
 * Interface for providing downstream system details to the contract domain.
 * This is a port for dependency inversion.
 */
public interface DownstreamProvider {
    List<String> getAvailableDownstreamSystems(TenantId tenantId);
    void pushToDownstream(String contractId, String systemId);
}
