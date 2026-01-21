package com.contract.master.contract.metadata.application;

import com.contract.master.contract.metadata.domain.model.FieldConfig;
import com.contract.master.contract.metadata.domain.repository.FieldConfigRepository;
import com.contract.master.security.TenantContext;
import com.contract.master.shared.domain.model.TenantId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.contract.master.contract.metadata.domain.event.FieldConfigChangedEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
public class FieldConfigService {

    @Autowired
    private FieldConfigRepository fieldConfigRepository;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    public List<FieldConfig> getConfigs() {
        return fieldConfigRepository.findAll();
    }

    @Transactional
    public void saveConfig(FieldConfig config) {
        String tenantId = TenantContext.getCurrentTenant();
        fieldConfigRepository.findAll().stream()
            .filter(c -> c.getFieldCode().equals(config.getFieldCode()) && !Objects.equals(c.getId(), config.getId()))
            .findFirst()
            .ifPresent(existing -> {
                throw new RuntimeException("Config already exists for field: " + config.getFieldCode());
            });

        fieldConfigRepository.save(config);
        eventPublisher.publishEvent(new FieldConfigChangedEvent(tenantId));
    }

    @Transactional
    public void saveConfigs(List<FieldConfig> configs) {
        String tenantId = TenantContext.getCurrentTenant();
        List<FieldConfig> existingConfigs = fieldConfigRepository.findAll();

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
                fieldConfigRepository.save(config);
            }
        }
        eventPublisher.publishEvent(new FieldConfigChangedEvent(tenantId));
    }
}
