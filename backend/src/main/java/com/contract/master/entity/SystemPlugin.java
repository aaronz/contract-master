package com.contract.master.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "system_plugin")
@Data
@EqualsAndHashCode(callSuper = true)
public class SystemPlugin extends BaseTenantEntity {
    @Id
    @Column(name = "plugin_id", length = 64)
    private String pluginId;

    @Column(name = "plugin_name", length = 128)
    private String pluginName;

    @Column(name = "plugin_type", length = 32)
    private String pluginType;

    @Column(name = "status")
    private Integer status;

    @Column(name = "config_json", columnDefinition = "TEXT")
    private String configJson;
}
