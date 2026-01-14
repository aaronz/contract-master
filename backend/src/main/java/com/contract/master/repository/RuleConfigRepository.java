package com.contract.master.repository;

import com.contract.master.entity.RuleConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RuleConfigRepository extends JpaRepository<RuleConfig, String> {
    List<RuleConfig> findByTenantIdAndIsEnabled(String tenantId, Boolean isEnabled);
}
