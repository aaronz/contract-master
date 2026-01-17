package com.contract.master.evaluation.domain.repository;

import com.contract.master.evaluation.domain.model.RuleConfig;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RuleConfigRepository extends JpaRepository<RuleConfig, String> {
    List<RuleConfig> findByTenantIdAndIsEnabled(String tenantId, Boolean isEnabled);
    List<RuleConfig> findByTenantId(String tenantId);
}
