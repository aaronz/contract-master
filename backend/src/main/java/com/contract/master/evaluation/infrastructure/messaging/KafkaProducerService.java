package com.contract.master.evaluation.infrastructure.messaging;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private static final String TOPIC = "evaluation-jobs"; // This topic should be configured in application.properties

    public KafkaProducerService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(KafkaProducerService.class);

    public void sendMessage(String message) {
        try {
            logger.info("Sending message to Kafka topic {}: {}", TOPIC, message);
            kafkaTemplate.send(TOPIC, message);
        } catch (Exception e) {
            logger.error("CRITICAL: Failed to send message to Kafka topic {}. Is Kafka running at localhost:9092?", TOPIC, e);
        }
    }
}
