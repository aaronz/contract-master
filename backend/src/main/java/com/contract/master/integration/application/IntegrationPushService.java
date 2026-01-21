package com.contract.master.integration.application;

import com.contract.master.contract.domain.model.Contract;
import com.contract.master.integration.domain.model.DownstreamSystem;
import com.contract.master.integration.domain.model.FieldMapping;
import com.contract.master.integration.domain.model.IntegrationEvent;
import com.contract.master.integration.domain.repository.DownstreamSystemRepository;
import com.contract.master.integration.domain.repository.FieldMappingRepository;
import com.contract.master.integration.domain.service.ScriptSandbox;
import com.contract.master.integration.infrastructure.IntegrationKafkaProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class IntegrationPushService {

    private static final Logger log = LoggerFactory.getLogger(IntegrationPushService.class);

    @Autowired
    private DownstreamSystemRepository systemRepository;

    @Autowired
    private FieldMappingRepository mappingRepository;

    @Autowired
    private IntegrationKafkaProducer kafkaProducer;

    @Autowired
    private ScriptSandbox scriptSandbox;

    public void pushToDownstreamSystems(Contract contract) {
        List<DownstreamSystem> systems = systemRepository.findAll();
        String tenantId = contract.getTenantId() != null ? contract.getTenantId().getId() : "default";

        for (DownstreamSystem system : systems) {
            if (!Boolean.TRUE.equals(system.getIsEnabled())) continue;

            log.info("Enqueuing integration event for contract {} to system: {}", contract.getContractId().value(), system.getSystemName());
            
            IntegrationEvent event = new IntegrationEvent(
                contract.getContractId().value().toString(),
                system.getSystemId(),
                tenantId
            );
            
            kafkaProducer.sendIntegrationEvent(event);
        }
    }

    public Map<String, Object> transformData(Contract contract, List<FieldMapping> mappings) {
        Map<String, Object> data = new HashMap<>();
        data.put("contract_id", contract.getContractId().value());
        data.put("contract_no", contract.getContractNo().value());
        data.put("contract_name", contract.getContractName());
        
        for (FieldMapping mapping : mappings) {
            if (!Boolean.TRUE.equals(mapping.getIsEnabled())) continue;
            
            Object value = getValueByFieldName(contract, mapping.getInternalField());
            if (value != null) {
                Object transformed = applyTransformation(value, mapping);
                data.put(mapping.getExternalField(), transformed);
            }
        }
        
        return data;
    }

    private Object getValueByFieldName(Contract contract, String fieldName) {
        if ("amount".equalsIgnoreCase(fieldName)) return contract.getAmount() != null ? contract.getAmount().getAmount() : null;
        if ("partyAName".equalsIgnoreCase(fieldName)) return contract.getPartyA() != null ? contract.getPartyA().getName() : null;
        if ("partyBName".equalsIgnoreCase(fieldName)) return contract.getPartyB() != null ? contract.getPartyB().getName() : null;
        return null;
    }

    private Object applyTransformation(Object value, FieldMapping mapping) {
        if (mapping.getTransformationScript() != null && !mapping.getTransformationScript().trim().isEmpty()) {
            return scriptSandbox.execute(mapping.getTransformationScript(), value);
        }

        String type = mapping.getTransformation();
        if (value == null || type == null || "NONE".equals(type)) return value;
        if ("UPPERCASE".equals(type)) return value.toString().toUpperCase();
        return value;
    }
}
