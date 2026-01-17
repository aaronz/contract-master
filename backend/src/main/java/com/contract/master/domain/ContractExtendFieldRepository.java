package com.contract.master.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContractExtendFieldRepository extends JpaRepository<ContractExtendField, String> {
    List<ContractExtendField> findByTenantId(String tenantId);
    java.util.Optional<ContractExtendField> findByTenantIdAndFieldCode(String tenantId, String fieldCode);
}
