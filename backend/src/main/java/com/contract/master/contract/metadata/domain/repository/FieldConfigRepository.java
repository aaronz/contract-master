package com.contract.master.contract.metadata.domain.repository;

import com.contract.master.contract.metadata.domain.model.FieldConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

import com.contract.master.shared.domain.model.TenantId;

@Repository
public interface FieldConfigRepository extends JpaRepository<FieldConfig, Long> {
    List<FieldConfig> findByTenantId(TenantId tenantId);
}
