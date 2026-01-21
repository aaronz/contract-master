package com.contract.master.integration.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.contract.master.shared.domain.model.BaseTenantEntity;
import com.contract.master.shared.infrastructure.persistence.TenantEntityListener;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "downstream_system")
@EntityListeners({TenantEntityListener.class})
public class DownstreamSystem extends BaseTenantEntity {
    @Column(name = "system_id", length = 64)
    private String systemId;

    @JsonProperty("systemName")
    @Column(name = "system_name", length = 128)
    private String systemName;

    @Column(name = "endpoint_url", length = 512)
    private String endpointUrl;

    @Column(name = "auth_type", length = 32)
    private String authType;

    @Column(name = "auth_config", columnDefinition = "TEXT")
    private String authConfig;

    @Column(name = "is_enabled")
    private Boolean isEnabled;

    @Column(name = "access_key", length = 64, unique = true)
    private String accessKey;

    @Column(name = "health_status", length = 32)
    private String healthStatus = "HEALTHY";

    @Column(name = "last_heartbeat")
    private LocalDateTime lastHeartbeat;

    public void generateAccessKey() {
        this.accessKey = "AK-" + UUID.randomUUID().toString().replace("-", "").toUpperCase();
    }

    public String getSystemId() { return systemId; }
    public void setSystemId(String systemId) { this.systemId = systemId; }
    public String getSystemName() { return systemName; }
    public void setSystemName(String systemName) { this.systemName = systemName; }
    public String getEndpointUrl() { return endpointUrl; }
    public void setEndpointUrl(String endpointUrl) { this.endpointUrl = endpointUrl; }
    public String getAuthType() { return authType; }
    public void setAuthType(String authType) { this.authType = authType; }
    public String getAuthConfig() { return authConfig; }
    public void setAuthConfig(String authConfig) { this.authConfig = authConfig; }
    public Boolean getIsEnabled() { return isEnabled; }
    public void setIsEnabled(Boolean enabled) { isEnabled = enabled; }
    public String getAccessKey() { return accessKey; }
    public void setAccessKey(String accessKey) { this.accessKey = accessKey; }
    public String getHealthStatus() { return healthStatus; }
    public void setHealthStatus(String healthStatus) { this.healthStatus = healthStatus; }
    public LocalDateTime getLastHeartbeat() { return lastHeartbeat; }
    public void setLastHeartbeat(LocalDateTime lastHeartbeat) { this.lastHeartbeat = lastHeartbeat; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        DownstreamSystem that = (DownstreamSystem) o;
        return Objects.equals(systemId, that.systemId) &&
               Objects.equals(systemName, that.systemName) &&
               Objects.equals(endpointUrl, that.endpointUrl) &&
               Objects.equals(authType, that.authType) &&
               Objects.equals(isEnabled, that.isEnabled);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), systemId, systemName, endpointUrl, authType, isEnabled);
    }
}
