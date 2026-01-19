package com.contract.master.problemcenter.infrastructure.messaging;

import com.contract.master.security.TenantContext;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class EvaluationProducer {

    private static final Logger log = LoggerFactory.getLogger(EvaluationProducer.class);
    private static final String TOPIC = "contract-evaluation";
    private final KafkaTemplate<String, String> kafkaTemplate;

    public EvaluationProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishJob(Long jobId) {
        try {
            String tenantId = TenantContext.getCurrentTenant();
            ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC, String.valueOf(jobId));
            if (tenantId != null) {
                record.headers().add("tenant-id", tenantId.getBytes(StandardCharsets.UTF_8));
            }
            log.info("Publishing evaluation job {} to Kafka topic {}", jobId, TOPIC);
            kafkaTemplate.send(record).whenComplete((result, ex) -> {
                if (ex == null) {
                    log.info("Successfully published job {} to Kafka", jobId);
                } else {
                    log.error("Failed to publish job {} to Kafka: {}", jobId, ex.getMessage());
                }
            });
        } catch (Exception e) {
            log.error("Error preparing Kafka message for job {}: {}", jobId, e.getMessage());
        }
    }
}
