package com.contract.master.ai.application;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
public class ExtractionTaskService {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void submitTask(String contractId) {
        String tenantId = com.contract.master.security.TenantContext.getCurrentTenant();
        ProducerRecord<String, String> record = new ProducerRecord<>("contract-extraction", contractId);
        if (tenantId != null) {
            record.headers().add("tenant-id", tenantId.getBytes(StandardCharsets.UTF_8));
        }
        kafkaTemplate.send(record);
    }

    @KafkaListener(topics = "contract-extraction", groupId = "contract-master-group")
    public void processTask(String contractId) {
    }
}
