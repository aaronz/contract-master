package com.contract.master.rule.interfaces.rest;

import com.contract.master.api.GlobalExceptionHandler;
import com.contract.master.rule.domain.model.Rule;
import com.contract.master.rule.domain.repository.RuleRepository;
import com.contract.master.shared.domain.model.TenantId;
import com.contract.master.security.TenantContext;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rules")
public class RuleController {

    private final RuleRepository ruleRepository;

    public RuleController(RuleRepository ruleRepository) {
        this.ruleRepository = ruleRepository;
    }

    @GetMapping
    public GlobalExceptionHandler.ApiResponse<List<Rule>> list() {
        TenantId tenantId = TenantId.of(TenantContext.getCurrentTenant());
        return GlobalExceptionHandler.ApiResponse.success(HttpStatus.OK, ruleRepository.findByTenantId(tenantId));
    }

    @PostMapping
    public GlobalExceptionHandler.ApiResponse<Rule> create(@RequestBody Rule rule) {
        rule.setTenantId(TenantId.of(TenantContext.getCurrentTenant()));
        return GlobalExceptionHandler.ApiResponse.success(HttpStatus.CREATED, ruleRepository.save(rule));
    }

    @GetMapping("/{id}")
    public GlobalExceptionHandler.ApiResponse<Rule> get(@PathVariable Long id) {
        return GlobalExceptionHandler.ApiResponse.success(HttpStatus.OK, ruleRepository.findById(id).orElse(null));
    }

    @PutMapping("/{id}")
    public GlobalExceptionHandler.ApiResponse<Rule> update(@PathVariable Long id, @RequestBody Rule rule) {
        return ruleRepository.findById(id).map(existing -> {
            if (!existing.getLogicContent().equals(rule.getLogicContent())) {
                existing.setVersion(existing.getVersion() + 1);
            }
            existing.setName(rule.getName());
            existing.setDescription(rule.getDescription());
            existing.setLogicContent(rule.getLogicContent());
            existing.setLogicType(rule.getLogicType());
            existing.setSeverity(rule.getSeverity());
            existing.setCategory(rule.getCategory());
            existing.setStatus(rule.getStatus());
            return GlobalExceptionHandler.ApiResponse.success(HttpStatus.OK, ruleRepository.save(existing));
        }).orElse(GlobalExceptionHandler.ApiResponse.error(HttpStatus.NOT_FOUND.value(), "Rule not found"));
    }
}
