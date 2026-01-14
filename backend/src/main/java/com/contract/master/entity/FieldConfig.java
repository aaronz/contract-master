package com.contract.master.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "field_config")
@Data
@EqualsAndHashCode(callSuper = true)
@EntityListeners({TenantEntityListener.class})
public class FieldConfig extends BaseTenantEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "config_type", length = 32)
    private String configType;

    @Column(name = "field_code", length = 64)
    private String fieldCode;

    @Column(name = "field_alias", length = 64)
    private String fieldAlias;

    @Column(name = "is_visible")
    private Boolean isVisible;

    @Column(name = "display_order")
    private Integer displayOrder;

    @Column(name = "required_role", length = 64)
    private String requiredRole;
}
