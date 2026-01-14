package com.contract.master.repository;

import com.contract.master.entity.ContractExtendField;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContractExtendFieldRepository extends JpaRepository<ContractExtendField, String> {
    List<ContractExtendField> findByTenantId(String tenantId);
}
