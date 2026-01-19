package com.contract.master.ai.domain.model;

import com.contract.master.shared.domain.model.BaseTenantEntity;
import com.contract.master.shared.infrastructure.persistence.TenantEntityListener;
import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "ai_setting")
@EntityListeners({TenantEntityListener.class})
public class AISetting extends BaseTenantEntity {

    @Column(name = "provider", length = 32)
    private String provider;

    @Column(name = "model_name", length = 64)
    private String modelName;

    @Column(name = "api_key", length = 256)
    private String apiKey;

    @Column(name = "endpoint_url", length = 512)
    private String endpointUrl;

    @Column(name = "extraction_prompt", columnDefinition = "TEXT")
    private String extractionPrompt;

    public String getProvider() { return provider; }
    public void setProvider(String provider) { this.provider = provider; }
    public String getModelName() { return modelName; }
    public void setModelName(String modelName) { this.modelName = modelName; }
    public String getApiKey() { return apiKey; }
    public void setApiKey(String apiKey) { this.apiKey = apiKey; }
    public String getEndpointUrl() { return endpointUrl; }
    public void setEndpointUrl(String endpointUrl) { this.endpointUrl = endpointUrl; }
    public String getExtractionPrompt() { return extractionPrompt; }
    public void setExtractionPrompt(String extractionPrompt) { this.extractionPrompt = extractionPrompt; }
}
