package com.contract.master.integration.infrastructure;

import com.contract.master.contract.application.ContractService;
import com.contract.master.contract.domain.model.Contract;
import com.contract.master.integration.application.IntegrationPushService;
import com.contract.master.integration.application.OAuth2TokenManager;
import com.contract.master.integration.domain.model.DownstreamSystem;
import com.contract.master.integration.domain.model.FieldMapping;
import com.contract.master.integration.domain.model.IntegrationEvent;
import com.contract.master.integration.domain.model.IntegrationLog;
import com.contract.master.integration.domain.repository.DownstreamSystemRepository;
import com.contract.master.integration.domain.repository.FieldMappingRepository;
import com.contract.master.integration.domain.repository.IntegrationLogRepository;
import com.contract.master.security.TenantContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Component
public class IntegrationKafkaConsumer {

    private static final Logger log = LoggerFactory.getLogger(IntegrationKafkaConsumer.class);

    @Autowired
    private ContractService contractService;

    @Autowired
    private DownstreamSystemRepository systemRepository;

    @Autowired
    private FieldMappingRepository mappingRepository;

    @Autowired
    private IntegrationLogRepository logRepository;

    @Autowired
    private IntegrationPushService pushService;

    @Autowired
    private OAuth2TokenManager tokenManager;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "contract-integration-events", groupId = "integration-group")
    public void consume(IntegrationEvent event) {
        log.info("Processing integration event for contract {} to system {}", event.getContractId(), event.getSystemId());
        
        try {
            TenantContext.setCurrentTenant(event.getTenantId());
            
            Contract contract = contractService.getRawContract(event.getContractId());
            DownstreamSystem system = systemRepository.findAll().stream()
                    .filter(s -> s.getSystemId().equals(event.getSystemId()))
                    .findFirst().orElseThrow(() -> new RuntimeException("System not found: " + event.getSystemId()));

            List<FieldMapping> mappings = mappingRepository.findByTargetSystemIdAndDirectionAndIsEnabledTrue(event.getSystemId(), "OUTBOUND");
            Map<String, Object> payload = pushService.transformData(contract, mappings);
            String payloadJson = objectMapper.writeValueAsString(payload);

            long start = System.currentTimeMillis();
            IntegrationLog integrationLog = new IntegrationLog();
            integrationLog.setSourceSystem(system.getSystemName());
            integrationLog.setEventType("OUTBOUND_PUSH");
            integrationLog.setRecordsCount(1);
            integrationLog.setRequestPayload(payloadJson);
            integrationLog.setRetryCount(event.getAttempt() - 1);
            integrationLog.setContractId(event.getContractId());
            integrationLog.setSystemId(event.getSystemId());

            try {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                
                String authType = system.getAuthType();
                if ("OAUTH2".equalsIgnoreCase(authType)) {
                    String token = tokenManager.getAccessToken(system);
                    headers.set("Authorization", "Bearer " + token);
                } else if (system.getAccessKey() != null) {
                    headers.set("X-API-KEY", system.getAccessKey());
                }

                HttpEntity<String> entity = new HttpEntity<>(payloadJson, headers);
                restTemplate.postForEntity(system.getEndpointUrl(), entity, String.class);
                
                integrationLog.setStatus("SUCCESS");
                log.info("Successfully pushed to {}", system.getSystemName());
            } catch (Exception e) {
                integrationLog.setStatus("FAILED");
                integrationLog.setErrorMessage(e.getMessage());
                log.error("Failed to push to system {}: {}", system.getSystemName(), e.getMessage());
                throw e;
            } finally {
                integrationLog.setDurationMs(System.currentTimeMillis() - start);
                logRepository.save(integrationLog);
            }
        } catch (Exception e) {
            log.error("Error processing integration event: {}", e.getMessage());
            throw new RuntimeException(e);
        } finally {
            TenantContext.clear();
        }
    }
}
