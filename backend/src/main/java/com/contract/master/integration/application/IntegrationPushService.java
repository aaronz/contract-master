package com.contract.master.integration.application;

import com.contract.master.contract.domain.model.Contract;
import com.contract.master.contract.dto.ContractDTO;
import com.contract.master.integration.domain.model.DownstreamSystem;
import com.contract.master.integration.domain.model.FieldMapping;
import com.contract.master.integration.domain.repository.DownstreamSystemRepository;
import com.contract.master.integration.domain.repository.FieldMappingRepository;
import com.contract.master.shared.domain.model.TenantId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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
    private com.contract.master.integration.domain.repository.IntegrationLogRepository logRepository;

    private final RestTemplate restTemplate = new RestTemplate();

    @Async
    public void pushToDownstreamSystems(Contract contract) {
        TenantId tenantId = contract.getTenantId();
        List<DownstreamSystem> systems = systemRepository.findByTenantId(tenantId);
        List<FieldMapping> mappings = mappingRepository.findByTenantId(tenantId);

        for (DownstreamSystem system : systems) {
            if (!Boolean.TRUE.equals(system.getIsEnabled())) continue;

            long start = System.currentTimeMillis();
            com.contract.master.integration.domain.model.IntegrationLog integrationLog = new com.contract.master.integration.domain.model.IntegrationLog();
            integrationLog.setSourceSystem(system.getSystemName());
            integrationLog.setEventType("OUTBOUND_PUSH");
            integrationLog.setTenantId(tenantId);
            integrationLog.setRecordsCount(1);

            try {
                log.info("Pushing contract {} to downstream system: {}", contract.getContractId().value(), system.getSystemName());
                Map<String, Object> payload = transformData(contract, mappings);
                
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                if (system.getAccessKey() != null) {
                    headers.set("X-API-KEY", system.getAccessKey());
                }

                HttpEntity<Map<String, Object>> entity = new HttpEntity<>(payload, headers);
                restTemplate.postForEntity(system.getEndpointUrl(), entity, String.class);
                
                integrationLog.setStatus("SUCCESS");
                log.info("Successfully pushed to {}", system.getSystemName());
            } catch (Exception e) {
                integrationLog.setStatus("FAILED");
                integrationLog.setErrorMessage(e.getMessage());
                log.error("Failed to push to system {}: {}", system.getSystemName(), e.getMessage());
            } finally {
                integrationLog.setDurationMs(System.currentTimeMillis() - start);
                logRepository.save(integrationLog);
            }
        }
    }

    private Map<String, Object> transformData(Contract contract, List<FieldMapping> mappings) {
        Map<String, Object> data = new HashMap<>();
        data.put("contract_id", contract.getContractId().value());
        data.put("contract_no", contract.getContractNo().value());
        data.put("contract_name", contract.getContractName());
        
        for (FieldMapping mapping : mappings) {
            if (!Boolean.TRUE.equals(mapping.getIsEnabled())) continue;
            
            Object value = getValueByFieldName(contract, mapping.getInternalField());
            if (value != null) {
                data.put(mapping.getExternalField(), applyTransformation(value, mapping.getTransformation()));
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

    private Object applyTransformation(Object value, String type) {
        if (value == null || type == null || "NONE".equals(type)) return value;
        if ("UPPERCASE".equals(type)) return value.toString().toUpperCase();
        return value;
    }
}
