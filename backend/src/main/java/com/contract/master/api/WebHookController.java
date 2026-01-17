package com.contract.master.api;

import com.contract.master.domain.WebHookConfig;
import com.contract.master.domain.WebHookConfigRepository;
import com.contract.master.security.TenantContext;
import com.contract.master.service.CrmIntegrationService;
import com.contract.master.service.RateLimiterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/webhook")
public class WebHookController {

    @Autowired
    private CrmIntegrationService crmIntegrationService;

    @Autowired
    private WebHookConfigRepository configRepository;

    @Autowired
    private RateLimiterService rateLimiterService;

    @PostMapping("/{source}")
    public GlobalExceptionHandler.ApiResponse<Void> receiveWebhook(@PathVariable String source, @RequestBody Map<String, Object> payload) {
        if (source == null || source.trim().isEmpty()) {
            throw new IllegalArgumentException("Source cannot be empty");
        }
        
        if (!rateLimiterService.tryConsume(source.toUpperCase())) {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Rate limit exceeded for source: " + source);
        }

        crmIntegrationService.syncContract(payload, source.toUpperCase());
        return GlobalExceptionHandler.ApiResponse.success(null);
    }

    @GetMapping("/configs")
    @PreAuthorize("hasRole('ADMIN')")
    public GlobalExceptionHandler.ApiResponse<List<WebHookConfig>> listConfigs() {
        return GlobalExceptionHandler.ApiResponse.success(configRepository.findByTenantId(TenantContext.getCurrentTenant()));
    }

    @PostMapping("/configs")
    @PreAuthorize("hasRole('ADMIN')")
    public GlobalExceptionHandler.ApiResponse<WebHookConfig> saveConfig(@RequestBody WebHookConfig config) {
        config.setTenantId(TenantContext.getCurrentTenant());
        return GlobalExceptionHandler.ApiResponse.success(configRepository.save(config));
    }

    @DeleteMapping("/configs/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public GlobalExceptionHandler.ApiResponse<Void> deleteConfig(@PathVariable Long id) {
        configRepository.deleteById(id);
        return GlobalExceptionHandler.ApiResponse.success(null);
    }
}
