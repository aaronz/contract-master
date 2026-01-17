package com.contract.master.api;

import com.contract.master.evaluation.domain.model.RuleConfig;
import com.contract.master.evaluation.domain.repository.RuleConfigRepository;
import com.contract.master.evaluation.application.RuleEngineService;
import com.contract.master.dto.ContractDTO;
import com.contract.master.security.TenantContext;
import com.contract.master.contract.application.ContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*; // Import all annotations from rest

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/rules")
public class RuleEngineController {

    @Autowired
    private RuleEngineService ruleEngineService;

    @Autowired
    private ContractService contractService;

    @Autowired
    private RuleConfigRepository ruleConfigRepository;

    @GetMapping
    public GlobalExceptionHandler.ApiResponse<List<RuleConfig>> list() {
        return GlobalExceptionHandler.ApiResponse.success(HttpStatus.OK, ruleConfigRepository.findByTenantId(TenantContext.getCurrentTenant()));
    }

    @GetMapping("/{id}")
    public GlobalExceptionHandler.ApiResponse<RuleConfig> get(@PathVariable String id) {
        return GlobalExceptionHandler.ApiResponse.success(HttpStatus.OK, ruleConfigRepository.findById(id).orElse(null));
    }

    @PostMapping
    public GlobalExceptionHandler.ApiResponse<RuleConfig> create(@RequestBody RuleConfig rule) {
        if (rule.getRuleId() == null) {
            rule.setRuleId(UUID.randomUUID().toString());
        }
        if (rule.getRuleType() == null) {
            rule.setRuleType("LOGIC");
        }
        rule.setTenantId(TenantContext.getCurrentTenant());
        return GlobalExceptionHandler.ApiResponse.success(HttpStatus.OK, ruleConfigRepository.save(rule));
    }

    @PutMapping("/{id}")
    public GlobalExceptionHandler.ApiResponse<RuleConfig> update(@PathVariable String id, @RequestBody RuleConfig rule) {
        rule.setRuleId(id);
        rule.setTenantId(TenantContext.getCurrentTenant());
        return GlobalExceptionHandler.ApiResponse.success(HttpStatus.OK, ruleConfigRepository.save(rule));
    }

    @PutMapping("/batch")
    public GlobalExceptionHandler.ApiResponse<List<RuleConfig>> batchUpdate(@RequestBody List<RuleConfig> rules) {
        String tenantId = TenantContext.getCurrentTenant();
        rules.forEach(rule -> rule.setTenantId(tenantId));
        return GlobalExceptionHandler.ApiResponse.success(HttpStatus.OK, ruleEngineService.batchUpdate(rules));
    }

    @PostMapping("/validate/{contractId}")
    public GlobalExceptionHandler.ApiResponse<Map<String, Object>> validateContract(@PathVariable String contractId) {
        ContractDTO contract = contractService.getContractById(contractId);
        Map<String, Object> responseData = new HashMap<>();
        
        if (contract != null) {
            List<String> violations = ruleEngineService.validate(contract);
            responseData.put("status", violations.isEmpty() ? "SUCCESS" : "VIOLATION");
            responseData.put("violations", violations);
        } else {
            responseData.put("status", "NOT_FOUND");
        }
        
        return GlobalExceptionHandler.ApiResponse.success(HttpStatus.OK, responseData);
    }

    @PostMapping("/ai-analyze/{contractId}")
    public GlobalExceptionHandler.ApiResponse<Map<String, Object>> aiAnalyze(@PathVariable String contractId) {
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("analysis", ruleEngineService.analyzeWithAI(contractId));
        return GlobalExceptionHandler.ApiResponse.success(HttpStatus.OK, responseData);
    }

    @GetMapping("/trigger-scenarios")
    public GlobalExceptionHandler.ApiResponse<Map<String, List<Map<String, String>>>> getTriggerScenarios() {
        List<Map<String, String>> scenarios = java.util.Arrays.asList(
            java.util.Map.of("name", "Contract Creation", "description", "Rules are evaluated automatically when a new contract is created."),
            java.util.Map.of("name", "Contract Update", "description", "Rules are evaluated automatically when an existing contract is updated."),
            java.util.Map.of("name", "Scheduled Review", "description", "Rules can be scheduled to run periodically against active contracts.")
        );
        return GlobalExceptionHandler.ApiResponse.success(HttpStatus.OK, java.util.Map.of("scenarios", scenarios));
    }
}
