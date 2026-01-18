package com.contract.master.shared.domain.model;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "system_plugin")
public class SystemPlugin extends BaseTenantEntity {
    @Column(name = "plugin_id", length = 64) // pluginId is now a regular field
    private String pluginId;

    @Column(name = "plugin_name", length = 128)
    private String pluginName;

    @Column(name = "plugin_type", length = 32)
    private String pluginType;

    @Column(name = "status")
    private Integer status;

    @Column(name = "config_json", columnDefinition = "TEXT")
    private String configJson;

    public String getPluginId() {
        return pluginId;
    }

    public void setPluginId(String pluginId) {
        this.pluginId = pluginId;
    }

    public String getPluginName() {
        return pluginName;
    }

    public void setPluginName(String pluginName) {
        this.pluginName = pluginName;
    }

    public String getPluginType() {
        return pluginType;
    }

    public void setPluginType(String pluginType) {
        this.pluginType = pluginType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getConfigJson() {
        return configJson;
    }

    public void setConfigJson(String configJson) {
        this.configJson = configJson;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false; // Compare parent class fields, including inherited ID
        SystemPlugin that = (SystemPlugin) o;
        return Objects.equals(pluginId, that.pluginId) &&
               Objects.equals(pluginName, that.pluginName) &&
               Objects.equals(pluginType, that.pluginType) &&
               Objects.equals(status, that.status) &&
               Objects.equals(configJson, that.configJson);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), pluginId, pluginName, pluginType, status, configJson);
    }
}
