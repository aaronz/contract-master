package com.contract.master.api;

import com.contract.master.domain.WebHookConfig;
import com.contract.master.domain.WebHookConfigRepository;
import com.contract.master.security.TenantContext;
import com.contract.master.service.CrmIntegrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/webhook")
public class WebHookController {

    @Autowired
    private CrmIntegrationService crmIntegrationService;

    @Autowired
    private WebHookConfigRepository configRepository;

    @PostMapping("/{source}")
    public GlobalExceptionHandler.ApiResponse<Void> receiveWebhook(@PathVariable String source, @RequestBody Map<String, Object> payload) {
        crmIntegrationService.syncContract(payload, source.toUpperCase());
        return GlobalExceptionHandler.ApiResponse.success(null);
    }

    @GetMapping("/configs")
    public GlobalExceptionHandler.ApiResponse<List<WebHookConfig>> listConfigs() {
        return GlobalExceptionHandler.ApiResponse.success(configRepository.findByTenantId(TenantContext.getCurrentTenant()));
    }

    @PostMapping("/configs")
    public GlobalExceptionHandler.ApiResponse<WebHookConfig> saveConfig(@RequestBody WebHookConfig config) {
        config.setTenantId(TenantContext.getCurrentTenant());
        return GlobalExceptionHandler.ApiResponse.success(configRepository.save(config));
    }

    @DeleteMapping("/configs/{id}")
    public GlobalExceptionHandler.ApiResponse<Void> deleteConfig(@PathVariable Long id) {
        configRepository.deleteById(id);
        return GlobalExceptionHandler.ApiResponse.success(null);
    }
}
