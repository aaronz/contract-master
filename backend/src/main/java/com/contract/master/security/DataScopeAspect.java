package com.contract.master.security;

import com.contract.master.shared.domain.model.DataPermissionRule;
import com.contract.master.shared.domain.repository.DataPermissionRuleRepository;
import com.contract.master.shared.domain.model.TenantId;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Aspect
@Component
public class DataScopeAspect {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private DataPermissionRuleRepository ruleRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Pointcut("execution(* com.contract.master.contract.domain.repository.ContractRepository..*(..))")
    public void contractRepositoryMethods() {}

    @Before("contractRepositoryMethods()")
    public void applyDataScope() {
        String tenantIdStr = TenantContext.getCurrentTenant();
        org.springframework.security.core.Authentication auth = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        
        if (tenantIdStr != null && auth != null) {
            List<String> roles = auth.getAuthorities().stream()
                    .map(a -> a.getAuthority().replace("ROLE_", ""))
                    .collect(java.util.stream.Collectors.toList());
            
            List<DataPermissionRule> rules = ruleRepository.findByIsEnabled(true);
            Session session = entityManager.unwrap(Session.class);
            
            List<DataPermissionRule> applicableRules = rules.stream()
                    .filter(r -> roles.contains(r.getRoleId()))
                    .collect(java.util.stream.Collectors.toList());

            if (roles.contains("admin")) {
                return;
            }

            for (DataPermissionRule rule : applicableRules) {
                try {
                    JsonNode condition = objectMapper.readTree(rule.getFilterCondition());
                    if (condition.has("dept_id")) {
                        List<String> deptIds = new ArrayList<>();
                        condition.get("dept_id").forEach(id -> deptIds.add(id.asText()));
                        if (!deptIds.isEmpty()) {
                            session.enableFilter("dataScopeFilter").setParameterList("deptIds", deptIds);
                        }
                    }
                    if (condition.has("owner_id")) {
                        String userId = condition.get("owner_id").asText();
                        if ("CURRENT_USER".equals(userId)) {
                            userId = auth.getName();
                        }
                        session.enableFilter("ownerScopeFilter").setParameter("userId", userId);
                    }
                } catch (Exception e) {
                }
            }
        }
    }
}
