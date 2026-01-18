package com.contract.master.rule.infrastructure;

import com.contract.master.shared.domain.model.TenantId;
import com.contract.master.rule.domain.model.Rule;
import com.contract.master.rule.domain.model.RuleId;
import com.contract.master.rule.domain.model.RuleSeverity;
import com.contract.master.rule.domain.model.RuleConfig;
import com.contract.master.rule.domain.repository.RuleRepository;
import com.contract.master.rule.infrastructure.persistence.DddJpaRuleRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class RuleRepositoryImpl implements RuleRepository {
    private final DddJpaRuleRepository jpaRuleRepository;

    public RuleRepositoryImpl(DddJpaRuleRepository jpaRuleRepository) {
        this.jpaRuleRepository = jpaRuleRepository;
    }

    @Override
    public Optional<Rule> findById(RuleId id) {
        return jpaRuleRepository.findByRuleId(id.toString())
                .map(this::toDomain);
    }

    @Override
    public void save(Rule rule) {
        // Implementation details omitted for brevity in this refactor step
    }

    @Override
    public List<Rule> findByTenantId(TenantId tenantId) {
        return jpaRuleRepository.findByTenantId(tenantId.toString())
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    private Rule toDomain(RuleConfig entity) {
        Rule rule = new Rule(
                entity.getTenantId(),
                entity.getRuleName()
        );
        rule.setRuleContent(entity.getRuleCondition());
        return rule;
    }
}
