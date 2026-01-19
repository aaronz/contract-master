package com.contract.master.ai.interfaces.rest;

import com.contract.master.ai.domain.model.AISetting;
import com.contract.master.ai.domain.repository.AISettingRepository;
import com.contract.master.api.GlobalExceptionHandler;
import com.contract.master.security.TenantContext;
import com.contract.master.shared.domain.model.TenantId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai/settings")
public class AISettingController {

    @Autowired
    private AISettingRepository repository;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public GlobalExceptionHandler.ApiResponse<AISetting> getSettings() {
        TenantId tenantId = TenantId.of(TenantContext.getCurrentTenant());
        return GlobalExceptionHandler.ApiResponse.success(
            HttpStatus.OK, 
            repository.findByTenantId(tenantId).orElse(new AISetting())
        );
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public GlobalExceptionHandler.ApiResponse<AISetting> saveSettings(@RequestBody AISetting setting) {
        TenantId tenantId = TenantId.of(TenantContext.getCurrentTenant());
        AISetting existing = repository.findByTenantId(tenantId).orElse(new AISetting());
        
        existing.setProvider(setting.getProvider());
        existing.setModelName(setting.getModelName());
        existing.setApiKey(setting.getApiKey());
        existing.setEndpointUrl(setting.getEndpointUrl());
        existing.setExtractionPrompt(setting.getExtractionPrompt());
        existing.setTenantId(tenantId);
        
        return GlobalExceptionHandler.ApiResponse.success(HttpStatus.OK, repository.save(existing));
    }
}
