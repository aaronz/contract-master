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
        kafkaTemplate.send("contract-extraction", contractId);
    }

    @KafkaListener(topics = "contract-extraction", groupId = "contract-master-group")
    public void processTask(String contractId) {
    }
}
