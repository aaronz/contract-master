package com.contract.master.rule.domain.repository;

import com.contract.master.rule.domain.model.Rule;
import com.contract.master.rule.domain.model.RuleStatus;
import com.contract.master.shared.domain.model.TenantId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RuleRepository extends JpaRepository<Rule, Long> {
    List<Rule> findByStatus(RuleStatus status);
}
