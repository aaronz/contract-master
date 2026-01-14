package com.contract.master.repository;

import com.contract.master.entity.DataPermissionRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DataPermissionRuleRepository extends JpaRepository<DataPermissionRule, String> {
    List<DataPermissionRule> findByTenantIdAndIsEnabled(String tenantId, Boolean isEnabled);
}
