package com.contract.master.shared.domain.repository;

import com.contract.master.shared.domain.model.DataPermissionRule;
import com.contract.master.shared.domain.model.TenantId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DataPermissionRuleRepository extends JpaRepository<DataPermissionRule, Long> {
    List<DataPermissionRule> findByTenantIdAndIsEnabled(TenantId tenantId, Boolean isEnabled);
}
