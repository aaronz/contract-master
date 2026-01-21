package com.contract.master.integration.infrastructure;

import com.contract.master.integration.domain.model.IntegrationEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class IntegrationKafkaProducer {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    public static final String TOPIC = "contract-integration-events";

    public void sendIntegrationEvent(IntegrationEvent event) {
        kafkaTemplate.send(TOPIC, event.getSystemId(), event);
    }
}
