package com.contract.master.api;

import com.contract.master.domain.RuleConfig;
import com.contract.master.domain.RuleConfigRepository;
import com.contract.master.dto.ContractDTO;
import com.contract.master.security.TenantContext;
import com.contract.master.service.ContractService;
import com.contract.master.service.RuleEngineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
        return GlobalExceptionHandler.ApiResponse.success(ruleConfigRepository.findByTenantId(TenantContext.getCurrentTenant()));
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
        return GlobalExceptionHandler.ApiResponse.success(ruleConfigRepository.save(rule));
    }

    @PutMapping("/{id}")
    public GlobalExceptionHandler.ApiResponse<RuleConfig> update(@PathVariable String id, @RequestBody RuleConfig rule) {
        rule.setRuleId(id);
        rule.setTenantId(TenantContext.getCurrentTenant());
        return GlobalExceptionHandler.ApiResponse.success(ruleConfigRepository.save(rule));
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
        
        return GlobalExceptionHandler.ApiResponse.success(responseData);
    }

    @PostMapping("/ai-analyze/{contractId}")
    public GlobalExceptionHandler.ApiResponse<Map<String, Object>> aiAnalyze(@PathVariable String contractId) {
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("analysis", ruleEngineService.analyzeWithAI(contractId));
        return GlobalExceptionHandler.ApiResponse.success(responseData);
    }
}
