package com.contract.master.rule.infrastructure.persistence;

import com.contract.master.rule.domain.model.RuleConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface DddJpaRuleRepository extends JpaRepository<RuleConfig, Long> {
    Optional<RuleConfig> findByRuleId(String ruleId);
}
