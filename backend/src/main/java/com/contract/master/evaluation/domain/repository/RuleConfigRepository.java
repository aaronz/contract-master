package com.contract.master.evaluation.domain.repository;

import com.contract.master.rule.domain.model.RuleConfig;
import com.contract.master.shared.domain.model.TenantId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RuleConfigRepository extends JpaRepository<RuleConfig, Long> {
    List<RuleConfig> findByIsEnabled(Boolean isEnabled);
    Optional<RuleConfig> findByRuleId(String ruleId);
}
