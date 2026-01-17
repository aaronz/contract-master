package com.contract.master.domain;

import com.contract.master.domain.BaseTenantEntity;
import com.contract.master.domain.TenantEntityListener;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

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

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    @NotBlank(message = "Config type is required")
    @Column(name = "config_type", length = 32)
    private String configType;

    @NotBlank(message = "Field code is required")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Field code must be alphanumeric or underscore")
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

    @Column(name = "field_color", length = 32)
    private String fieldColor;

    @Column(name = "field_styles", length = 128)
    private String fieldStyles;

    @Column(name = "api_return")
    private Boolean apiReturn;

    public void setFieldCode(String fieldCode) { this.fieldCode = fieldCode; }
    public void setConfigType(String configType) { this.configType = configType; }
    public void setFieldAlias(String fieldAlias) { this.fieldAlias = fieldAlias; }
    public void setIsVisible(Boolean isVisible) { this.isVisible = isVisible; }
    public Integer getDisplayOrder() { return displayOrder; }
    public void setDisplayOrder(Integer displayOrder) { this.displayOrder = displayOrder; }
    public void setRequiredRole(String requiredRole) { this.requiredRole = requiredRole; }
    public void setFieldColor(String fieldColor) { this.fieldColor = fieldColor; }
    public void setFieldStyles(String fieldStyles) { this.fieldStyles = fieldStyles; }
    public void setApiReturn(Boolean apiReturn) { this.apiReturn = apiReturn; }

    public String getConfigType() { return configType; }
    public String getFieldCode() { return fieldCode; }
    public String getFieldAlias() { return fieldAlias; }
    public Boolean getIsVisible() { return isVisible; }
    public String getRequiredRole() { return requiredRole; }
    public String getFieldColor() { return fieldColor; }
    public String getFieldStyles() { return fieldStyles; }
    public Boolean getApiReturn() { return apiReturn; }
}
