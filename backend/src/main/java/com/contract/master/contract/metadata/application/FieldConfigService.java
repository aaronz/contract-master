package com.contract.master.contract.metadata.application;

import com.contract.master.contract.metadata.domain.model.FieldConfig;
import com.contract.master.contract.metadata.domain.repository.FieldConfigRepository;
import com.contract.master.security.TenantContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import com.contract.master.contract.application.ContractService;

@Service
public class FieldConfigService {

    @Autowired
    private FieldConfigRepository fieldConfigRepository;

    @Autowired
    private ContractService contractService;

    public List<FieldConfig> getConfigs() {
        String tenantId = TenantContext.getCurrentTenant();
        if (tenantId == null) {
            return Collections.emptyList();
        }
        return fieldConfigRepository.findByTenantId(tenantId);
    }

    @Transactional
    public void saveConfig(FieldConfig config) {
        String tenantId = TenantContext.getCurrentTenant();
        fieldConfigRepository.findByTenantId(tenantId).stream()
            .filter(c -> c.getFieldCode().equals(config.getFieldCode()) && !Objects.equals(c.getId(), config.getId()))
            .findFirst()
            .ifPresent(existing -> {
                throw new RuntimeException("Config already exists for field: " + config.getFieldCode());
            });

        if (config.getId() != null) {
            fieldConfigRepository.findById(config.getId()).ifPresent(existing -> {
                if (!existing.getTenantId().equals(tenantId)) {
                    throw new RuntimeException("Unauthorized to update config for another tenant");
                }
            });
        }
        config.setTenantId(tenantId);
        fieldConfigRepository.save(config);
        contractService.clearFieldConfigCache();
    }

    @Transactional
    public void saveConfigs(List<FieldConfig> configs) {
        String tenantId = TenantContext.getCurrentTenant();
        List<FieldConfig> existingConfigs = fieldConfigRepository.findByTenantId(tenantId);

        for (FieldConfig config : configs) {
            FieldConfig existing = null;
            if (config.getId() != null) {
                existing = existingConfigs.stream().filter(c -> c.getId().equals(config.getId())).findFirst().orElse(null);
            } else {
                existing = existingConfigs.stream().filter(c -> c.getFieldCode().equals(config.getFieldCode())).findFirst().orElse(null);
            }

            if (existing != null) {
                existing.setFieldAlias(config.getFieldAlias());
                existing.setIsVisible(config.getIsVisible());
                existing.setApiReturn(config.getApiReturn());
                existing.setDisplayOrder(config.getDisplayOrder());
                existing.setFieldColor(config.getFieldColor());
                existing.setFieldStyles(config.getFieldStyles());
                existing.setRequiredRole(config.getRequiredRole());
                fieldConfigRepository.save(existing);
            } else {
                config.setTenantId(tenantId);
                fieldConfigRepository.save(config);
            }
        }
        contractService.clearFieldConfigCache();
    }
}
