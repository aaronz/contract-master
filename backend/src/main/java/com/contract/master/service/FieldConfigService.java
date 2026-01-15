package com.contract.master.service;

import com.contract.master.domain.FieldConfig;
import com.contract.master.domain.FieldConfigRepository;
import com.contract.master.security.TenantContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class FieldConfigService {

    @Autowired
    private FieldConfigRepository fieldConfigRepository;

    public List<FieldConfig> getConfigs() {
        return fieldConfigRepository.findByTenantId(TenantContext.getCurrentTenant());
    }

    public void saveConfig(FieldConfig config) {
        fieldConfigRepository.save(config);
    }
}
