package com.contract.master.integration.infrastructure;

import com.contract.master.contract.domain.DownstreamProvider;
import com.contract.master.shared.domain.model.TenantId;
import com.contract.master.integration.domain.model.DownstreamSystem;
import com.contract.master.integration.domain.repository.DownstreamSystemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ContractDownstreamProviderImpl implements DownstreamProvider {

    @Autowired
    private DownstreamSystemRepository downstreamSystemRepository;

    @Override
    public List<String> getAvailableDownstreamSystems(TenantId tenantId) {
        // Implementation logic
        return downstreamSystemRepository.findAll().stream()
                .map(DownstreamSystem::getSystemId)
                .collect(Collectors.toList());
    }

    @Override
    public void pushToDownstream(String contractId, String systemId) {
        // Implementation logic for pushing to CRM/ERP
    }
}
