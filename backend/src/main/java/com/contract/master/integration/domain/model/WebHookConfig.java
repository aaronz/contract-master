package com.contract.master.integration.domain.model;

import com.contract.master.shared.domain.model.BaseTenantEntity;
import com.contract.master.shared.infrastructure.persistence.TenantEntityListener;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "webhook_config")
@Data
@EqualsAndHashCode(callSuper = true)
@EntityListeners({TenantEntityListener.class})
public class WebHookConfig extends BaseTenantEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 128)
    private String name;

    @Column(name = "url", length = 512)
    private String url;

    @Column(name = "events", columnDefinition = "TEXT")
    private String events;

    @Column(name = "auth_type", length = 32)
    private String authType;

    @Column(name = "is_enabled")
    private Boolean isEnabled;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
    public String getEvents() { return events; }
    public void setEvents(String events) { this.events = events; }
    public String getAuthType() { return authType; }
    public void setAuthType(String authType) { this.authType = authType; }
    public Boolean getIsEnabled() { return isEnabled; }
    public void setIsEnabled(Boolean isEnabled) { this.isEnabled = isEnabled; }
}
