package com.contract.master.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "downstream_system")
@Data
@EqualsAndHashCode(callSuper = true)
public class DownstreamSystem extends BaseTenantEntity {
    @Id
    @Column(name = "system_id", length = 64)
    private String systemId;

    @Column(name = "system_name", length = 128)
    private String systemName;

    @Column(name = "access_key", length = 64, unique = true)
    private String accessKey;

    @Column(name = "publish_scope", columnDefinition = "TEXT")
    private String publishScope;

    @Column(name = "is_enabled")
    private Boolean isEnabled;
}
