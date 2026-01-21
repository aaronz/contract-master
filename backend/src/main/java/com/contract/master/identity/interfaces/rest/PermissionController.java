package com.contract.master.identity.interfaces.rest;

import com.contract.master.api.GlobalExceptionHandler;
import com.contract.master.shared.domain.model.DataPermissionRule;
import com.contract.master.shared.domain.repository.DataPermissionRuleRepository;
import com.contract.master.security.TenantContext;
import com.contract.master.shared.domain.model.TenantId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/permissions")
public class PermissionController {

    @Autowired
    private DataPermissionRuleRepository dataRuleRepository;

    @GetMapping("/matrix")
    public GlobalExceptionHandler.ApiResponse<Map<String, Map<String, Object>>> getPermissionMatrix() {
        List<DataPermissionRule> dataRules = dataRuleRepository.findByIsEnabled(true);
        
        Map<String, Map<String, Object>> matrix = new HashMap<>();
        
        for (DataPermissionRule rule : dataRules) {
            String roleId = rule.getRoleId();
            String moduleId = rule.getDataType();
            
            matrix.computeIfAbsent(roleId, k -> new HashMap<>())
                  .put(moduleId, Map.of(
                      "enabled", rule.getIsEnabled(),
                      "scope", parseScopeFromCondition(rule.getFilterCondition())
                  ));
        }
        
        return GlobalExceptionHandler.ApiResponse.success(HttpStatus.OK, matrix);
    }

    @PostMapping("/matrix")
    public GlobalExceptionHandler.ApiResponse<Void> savePermissionMatrix(@RequestBody Map<String, Map<String, Map<String, Object>>> matrix) {
        matrix.forEach((roleId, modules) -> {
            modules.forEach((modId, config) -> {
                boolean enabled = (boolean) config.get("enabled");
                String scope = (String) config.get("scope");
                
                Optional<DataPermissionRule> existing = dataRuleRepository.findByIsEnabled(true).stream()
                        .filter(r -> r.getRoleId().equals(roleId) && r.getDataType().equals(modId))
                        .findFirst();
                
                DataPermissionRule rule = existing.orElse(new DataPermissionRule());
                rule.setRoleId(roleId);
                rule.setDataType(modId);
                rule.setRuleName(roleId + " " + modId + " Permission");
                rule.setIsEnabled(enabled);
                rule.setFilterCondition(generateCondition(scope));
                
                dataRuleRepository.save(rule);
            });
        });
        
        return GlobalExceptionHandler.ApiResponse.success(HttpStatus.OK, null);
    }

    private String parseScopeFromCondition(String condition) {
        if (condition == null || condition.isEmpty()) return "all";
        if (condition.contains("dept_id")) return "dept";
        if (condition.contains("owner_id")) return "self";
        return "all";
    }

    private String generateCondition(String scope) {
        if ("dept".equals(scope)) return "{\"dept_id\": [\"CURRENT_DEPT\"]}";
        if ("self".equals(scope)) return "{\"owner_id\": \"CURRENT_USER\"}";
        return "{}";
    }
}
