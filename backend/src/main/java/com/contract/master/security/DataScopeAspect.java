package com.contract.master.security;

import com.contract.master.entity.DataPermissionRule;
import com.contract.master.repository.DataPermissionRuleRepository;
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

    @Pointcut("execution(* com.contract.master.repository.ContractBaseRepository..*(..))")
    public void contractRepositoryMethods() {}

    @Before("contractRepositoryMethods()")
    public void applyDataScope() {
        String tenantId = TenantContext.getCurrentTenant();
        if (tenantId != null) {
            List<DataPermissionRule> rules = ruleRepository.findByTenantIdAndIsEnabled(tenantId, true);
            Session session = entityManager.unwrap(Session.class);
            
            for (DataPermissionRule rule : rules) {
                try {
                    JsonNode condition = objectMapper.readTree(rule.getFilterCondition());
                    if (condition.has("dept_id")) {
                        List<String> deptIds = new ArrayList<>();
                        condition.get("dept_id").forEach(id -> deptIds.add(id.asText()));
                        if (!deptIds.isEmpty()) {
                            session.enableFilter("dataScopeFilter").setParameterList("deptIds", deptIds);
                        }
                    }
                } catch (Exception e) {
                }
            }
        }
    }
}
