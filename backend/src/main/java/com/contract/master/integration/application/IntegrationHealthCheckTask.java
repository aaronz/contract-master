package com.contract.master.integration.application;

import com.contract.master.integration.domain.model.DownstreamSystem;
import com.contract.master.integration.domain.repository.DownstreamSystemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class IntegrationHealthCheckTask {

    private static final Logger log = LoggerFactory.getLogger(IntegrationHealthCheckTask.class);

    @Autowired
    private DownstreamSystemRepository systemRepository;

    private final RestTemplate restTemplate = new RestTemplate();

    @Scheduled(fixedRate = 300000)
    public void checkDownstreamHealth() {
        List<DownstreamSystem> systems = systemRepository.findAll();
        
        for (DownstreamSystem system : systems) {
            if (!Boolean.TRUE.equals(system.getIsEnabled())) continue;

            try {
                ResponseEntity<String> response = restTemplate.getForEntity(system.getEndpointUrl() + "/health", String.class);
                if (response.getStatusCode().is2xxSuccessful()) {
                    system.setHealthStatus("HEALTHY");
                } else {
                    system.setHealthStatus("UNSTABLE");
                }
            } catch (Exception e) {
                log.warn("Health check failed for system {}: {}", system.getSystemName(), e.getMessage());
                system.setHealthStatus("DOWN");
            } finally {
                system.setLastHeartbeat(LocalDateTime.now());
                systemRepository.save(system);
            }
        }
    }
}
