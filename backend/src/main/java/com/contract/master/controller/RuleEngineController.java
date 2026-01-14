package com.contract.master.controller;

import com.contract.master.dto.ContractDTO;
import com.contract.master.service.ContractService;
import com.contract.master.service.RuleEngineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/rules")
public class RuleEngineController {

    @Autowired
    private RuleEngineService ruleEngineService;

    @Autowired
    private ContractService contractService;

    @PostMapping("/validate/{contractId}")
    public Map<String, Object> validateContract(@PathVariable String contractId) {
        ContractDTO contract = contractService.getContractById(contractId);
        Map<String, Object> response = new HashMap<>();
        
        if (contract != null) {
            List<String> violations = ruleEngineService.validate(contract);
            response.put("status", violations.isEmpty() ? "SUCCESS" : "VIOLATION");
            response.put("violations", violations);
        } else {
            response.put("status", "NOT_FOUND");
        }
        
        return response;
    }

    @PostMapping("/ai-analyze/{contractId}")
    public Map<String, String> aiAnalyze(@PathVariable String contractId) {
        Map<String, String> response = new HashMap<>();
        response.put("analysis", ruleEngineService.analyzeWithAI(contractId));
        return response;
    }
}
