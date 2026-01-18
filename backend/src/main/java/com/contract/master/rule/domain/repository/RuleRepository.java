package com.contract.master.rule.domain.repository;

import com.contract.master.rule.domain.model.Rule;
import com.contract.master.rule.domain.model.RuleId;
import com.contract.master.shared.domain.model.TenantId;
import java.util.List;
import java.util.Optional;

public interface RuleRepository {
    Optional<Rule> findById(RuleId id);
    void save(Rule rule);
    List<Rule> findByTenantId(TenantId tenantId);
}
