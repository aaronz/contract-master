package com.contract.master.contract.domain.repository;

import com.contract.master.contract.domain.model.Contract;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ContractRepository {
    Optional<Contract> findById(String id);
    Contract save(Contract contract);
    Page<Contract> findAll(Pageable pageable);
    Page<Contract> findByTenantId(String tenantId, Pageable pageable);
}
