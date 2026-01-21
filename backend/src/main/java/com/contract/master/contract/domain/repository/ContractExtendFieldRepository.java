package com.contract.master.contract.domain.repository;

import com.contract.master.contract.domain.model.ContractExtendField;
import com.contract.master.shared.domain.model.TenantId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContractExtendFieldRepository extends JpaRepository<ContractExtendField, Long> {
    java.util.Optional<ContractExtendField> findByFieldCode(String fieldCode);
}
