package com.contract.master.integration.application;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.contract.master.integration.domain.model.DownstreamSystem;
import com.contract.master.integration.domain.repository.DownstreamSystemRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

@Service
public class OAuth2TokenManager {

    @Autowired
    private DownstreamSystemRepository systemRepository;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final Cache<String, String> tokenCache = Caffeine.newBuilder()
            .expireAfterWrite(50, TimeUnit.MINUTES)
            .maximumSize(100)
            .build();

    public String getAccessToken(DownstreamSystem system) {
        if (!"OAUTH2".equalsIgnoreCase(system.getAuthType())) {
            return null;
        }

        return tokenCache.get(system.getSystemId(), k -> fetchNewToken(system));
    }

    private String fetchNewToken(DownstreamSystem system) {
        try {
            JsonNode config = objectMapper.readTree(system.getAuthConfig());
            String tokenUrl = config.path("tokenUrl").asText();
            String clientId = config.path("clientId").asText();
            String clientSecret = config.path("clientSecret").asText();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("grant_type", "client_credentials");
            map.add("client_id", clientId);
            map.add("client_secret", clientSecret);

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
            String response = restTemplate.postForObject(tokenUrl, request, String.class);
            
            return objectMapper.readTree(response).path("access_token").asText();
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch OAuth2 token: " + e.getMessage());
        }
    }
}
