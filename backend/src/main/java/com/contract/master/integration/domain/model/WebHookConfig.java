package com.contract.master.integration.domain.model;

import com.contract.master.shared.domain.model.BaseTenantEntity;
import com.contract.master.shared.infrastructure.persistence.TenantEntityListener;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "webhook_config")
@EntityListeners({TenantEntityListener.class})
public class WebHookConfig extends BaseTenantEntity {
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

    // No need for webhookId and its getters/setters here, as id is inherited from BaseEntity
    // Adjust getters/setters to use inherited getId()/setId() or equivalent if business logic requires a different name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getEvents() {
        return events;
    }

    public void setEvents(String events) {
        this.events = events;
    }

    public String getAuthType() {
        return authType;
    }

    public void setAuthType(String authType) {
        this.authType = authType;
    }

    public Boolean getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(Boolean enabled) {
        isEnabled = enabled;
    }

    // Since id is inherited, equals and hashCode should use inherited getId()
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false; // Compare parent class fields
        WebHookConfig that = (WebHookConfig) o;
        // Compare fields specific to WebHookConfig, excluding the inherited ID
        return Objects.equals(name, that.name) &&
               Objects.equals(url, that.url) &&
               Objects.equals(events, that.events) &&
               Objects.equals(authType, that.authType) &&
               Objects.equals(isEnabled, that.isEnabled);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, url, events, authType, isEnabled);
    }
}

