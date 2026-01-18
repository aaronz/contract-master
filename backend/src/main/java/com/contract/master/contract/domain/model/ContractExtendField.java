package com.contract.master.contract.domain.model;

import com.contract.master.shared.domain.model.BaseTenantEntity;
import com.contract.master.shared.infrastructure.persistence.TenantEntityListener;

import jakarta.persistence.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.util.Objects;

@Entity
@Table(name = "contract_extend_field")
@EntityListeners({AuditingEntityListener.class, TenantEntityListener.class})
public class ContractExtendField extends BaseTenantEntity {
    @Column(name = "field_id", length = 64) // fieldId is now a regular field
    private String fieldId;

    @NotBlank(message = "Field name is required")
    @Column(name = "field_name", length = 64)
    private String fieldName;

    @NotBlank(message = "Field code is required")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Field code must be alphanumeric or underscore")
    @Column(name = "field_code", length = 64)
    private String fieldCode;

    @NotBlank(message = "Field type is required")
    @Column(name = "field_type", length = 32)
    private String fieldType;

    @Column(name = "is_required")
    private Boolean isRequired;

    @Column(name = "default_value", length = 255)
    private String defaultValue;

    @Column(name = "remark", length = 512)
    private String remark;

    public String getFieldId() {
        return fieldId;
    }

    public void setFieldId(String fieldId) {
        this.fieldId = fieldId;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldCode() {
        return fieldCode;
    }

    public void setFieldCode(String fieldCode) {
        this.fieldCode = fieldCode;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public Boolean getIsRequired() {
        return isRequired;
    }

    public void setIsRequired(Boolean required) {
        isRequired = required;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false; // Compare parent class fields, including inherited ID
        ContractExtendField that = (ContractExtendField) o;
        return Objects.equals(fieldId, that.fieldId) &&
               Objects.equals(fieldName, that.fieldName) &&
               Objects.equals(fieldCode, that.fieldCode) &&
               Objects.equals(fieldType, that.fieldType) &&
               Objects.equals(isRequired, that.isRequired) &&
               Objects.equals(defaultValue, that.defaultValue) &&
               Objects.equals(remark, that.remark);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), fieldId, fieldName, fieldCode, fieldType, isRequired, defaultValue, remark);
    }
}
