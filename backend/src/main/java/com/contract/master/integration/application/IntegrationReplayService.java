package com.contract.master.integration.application;

import com.contract.master.integration.domain.model.IntegrationEvent;
import com.contract.master.integration.domain.model.IntegrationLog;
import com.contract.master.integration.domain.repository.IntegrationLogRepository;
import com.contract.master.integration.infrastructure.IntegrationKafkaProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class IntegrationReplayService {

    @Autowired
    private IntegrationLogRepository logRepository;

    @Autowired
    private IntegrationKafkaProducer kafkaProducer;

    @Transactional
    public void replay(Long logId) {
        IntegrationLog logEntry = logRepository.findById(logId)
                .orElseThrow(() -> new RuntimeException("Integration log not found: " + logId));

        try {
            IntegrationEvent event = new IntegrationEvent(
                logEntry.getContractId(),
                logEntry.getSystemId(),
                logEntry.getTenantId().getId()
            );
            
            kafkaProducer.sendIntegrationEvent(event);
        } catch (Exception e) {
            throw new RuntimeException("Replay failed: " + e.getMessage());
        }
    }
}
