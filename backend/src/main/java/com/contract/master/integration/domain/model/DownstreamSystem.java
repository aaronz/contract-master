package com.contract.master.integration.domain.model;

import com.contract.master.domain.BaseTenantEntity;
import com.contract.master.domain.TenantEntityListener;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "downstream_system")
@Data
@EqualsAndHashCode(callSuper = true)
@EntityListeners({TenantEntityListener.class})
public class DownstreamSystem extends BaseTenantEntity {
    @Id
    @Column(name = "system_id", length = 64)
    private String systemId;

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

    public void setSystemId(String systemId) { this.systemId = systemId; }
    public String getSystemId() { return systemId; }
    public String getAccessKey() { return accessKey; }
    public void setAccessKey(String accessKey) { this.accessKey = accessKey; }
    public Boolean getIsEnabled() { return isEnabled; }
    public void setIsEnabled(Boolean isEnabled) { this.isEnabled = isEnabled; }
    public String getSystemName() { return systemName; }
    public void setSystemName(String systemName) { this.systemName = systemName; }
}
