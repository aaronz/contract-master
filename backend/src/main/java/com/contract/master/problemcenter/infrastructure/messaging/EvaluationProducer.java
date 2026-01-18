package com.contract.master.problemcenter.infrastructure.messaging;

import com.contract.master.security.TenantContext;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class EvaluationProducer {

    private static final String TOPIC = "contract-evaluation";
    private final KafkaTemplate<String, String> kafkaTemplate;

    public EvaluationProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishJob(Long jobId) {
        String tenantId = TenantContext.getCurrentTenant();
        ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC, String.valueOf(jobId));
        if (tenantId != null) {
            record.headers().add("tenant-id", tenantId.getBytes(StandardCharsets.UTF_8));
        }
        kafkaTemplate.send(record);
    }
}
