package com.contract.master.api;

import com.contract.master.domain.FieldConfig;
import com.contract.master.service.FieldConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/settings/fields")
public class FieldConfigController {

    @Autowired
    private FieldConfigService fieldConfigService;

    @GetMapping
    public GlobalExceptionHandler.ApiResponse<List<FieldConfig>> getConfigs() {
        return GlobalExceptionHandler.ApiResponse.success(fieldConfigService.getConfigs());
    }

    @PostMapping
    public GlobalExceptionHandler.ApiResponse<Void> saveConfig(@Valid @RequestBody FieldConfig config) {
        fieldConfigService.saveConfig(config);
        return GlobalExceptionHandler.ApiResponse.success(null);
    }

    @PostMapping("/batch")
    public GlobalExceptionHandler.ApiResponse<Void> saveConfigs(@Valid @RequestBody List<FieldConfig> configs) {
        fieldConfigService.saveConfigs(configs);
        return GlobalExceptionHandler.ApiResponse.success(null);
    }
}
