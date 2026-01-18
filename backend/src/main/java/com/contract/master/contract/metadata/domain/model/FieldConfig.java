package com.contract.master.contract.metadata.domain.model;

import com.contract.master.shared.domain.model.BaseTenantEntity;
import com.contract.master.shared.infrastructure.persistence.TenantEntityListener;
import jakarta.persistence.*;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.util.Objects;

@Entity
@Table(name = "field_config")
@EntityListeners({TenantEntityListener.class})
public class FieldConfig extends BaseTenantEntity {
    // ID is now inherited from BaseEntity
    // No need for explicit ID field here

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

    public String getConfigType() {
        return configType;
    }

    public void setConfigType(String configType) {
        this.configType = configType;
    }

    public String getFieldCode() {
        return fieldCode;
    }

    public void setFieldCode(String fieldCode) {
        this.fieldCode = fieldCode;
    }

    public String getFieldAlias() {
        return fieldAlias;
    }

    public void setFieldAlias(String fieldAlias) {
        this.fieldAlias = fieldAlias;
    }

    public Boolean getIsVisible() {
        return isVisible;
    }

    public void setIsVisible(Boolean visible) {
        isVisible = visible;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public String getRequiredRole() {
        return requiredRole;
    }

    public void setRequiredRole(String requiredRole) {
        this.requiredRole = requiredRole;
    }

    public String getFieldColor() {
        return fieldColor;
    }

    public void setFieldColor(String fieldColor) {
        this.fieldColor = fieldColor;
    }

    public String getFieldStyles() {
        return fieldStyles;
    }

    public void setFieldStyles(String fieldStyles) {
        this.fieldStyles = fieldStyles;
    }

    public Boolean getApiReturn() {
        return apiReturn;
    }

    public void setApiReturn(Boolean apiReturn) {
        this.apiReturn = apiReturn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false; // Compare parent class fields, including inherited ID
        FieldConfig that = (FieldConfig) o;
        return Objects.equals(configType, that.configType) &&
               Objects.equals(fieldCode, that.fieldCode) &&
               Objects.equals(fieldAlias, that.fieldAlias) &&
               Objects.equals(isVisible, that.isVisible) &&
               Objects.equals(displayOrder, that.displayOrder) &&
               Objects.equals(requiredRole, that.requiredRole) &&
               Objects.equals(fieldColor, that.fieldColor) &&
               Objects.equals(fieldStyles, that.fieldStyles) &&
               Objects.equals(apiReturn, that.apiReturn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), configType, fieldCode, fieldAlias, isVisible, displayOrder, requiredRole, fieldColor, fieldStyles, apiReturn);
    }
}
