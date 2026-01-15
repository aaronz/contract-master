package com.contract.master.domain;

import com.contract.master.domain.BaseTenantEntity;
import com.contract.master.domain.TenantEntityListener;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "field_config")
@Data
@EqualsAndHashCode(callSuper = true)
@EntityListeners({TenantEntityListener.class})
public class FieldConfig extends BaseTenantEntity {
    @Override
    public String getTenantId() { return super.getTenantId(); }
    @Override
    public void setTenantId(String tenantId) { super.setTenantId(tenantId); }

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

    public String getConfigType() { return configType; }
    public String getFieldCode() { return fieldCode; }
    public String getFieldAlias() { return fieldAlias; }
    public Boolean getIsVisible() { return isVisible; }
    public String getRequiredRole() { return requiredRole; }
}
