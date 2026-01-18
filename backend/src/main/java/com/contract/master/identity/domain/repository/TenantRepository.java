package com.contract.master.identity.domain.repository;

import com.contract.master.identity.domain.model.Tenant;
import com.contract.master.shared.domain.model.TenantId;
import java.util.Optional;

public interface TenantRepository {
    Optional<Tenant> findById(TenantId id);
    void save(Tenant tenant);
}
