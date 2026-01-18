package com.contract.master.evaluation.domain.repository;

import com.contract.master.rule.domain.model.RuleConfig;
import com.contract.master.shared.domain.model.TenantId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RuleConfigRepository extends JpaRepository<RuleConfig, Long> {
    List<RuleConfig> findByTenantIdAndIsEnabled(TenantId tenantId, Boolean isEnabled);
    List<RuleConfig> findByTenantId(TenantId tenantId);
}
