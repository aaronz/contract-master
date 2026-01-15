package com.contract.master.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ExtractionTaskService {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void submitTask(String contractId) {
        String tenantId = com.contract.master.security.TenantContext.getCurrentTenant();
        String message = tenantId + ":" + contractId;
        kafkaTemplate.send("contract-extraction", message);
    }

    @KafkaListener(topics = "contract-extraction", groupId = "contract-master-group")
    public void processTask(String message) {
        if (message == null || !message.contains(":")) return;
        String[] parts = message.split(":");
        String tenantId = parts[0];
        String contractId = parts[1];
        
        try {
            com.contract.master.security.TenantContext.setCurrentTenant(tenantId);
        } finally {
            com.contract.master.security.TenantContext.clear();
        }
    }
}
