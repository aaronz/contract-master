package com.contract.master.integration.domain.model;

import com.contract.master.shared.domain.model.BaseTenantEntity;
import com.contract.master.shared.infrastructure.persistence.TenantEntityListener;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "field_mapping")
@EntityListeners({TenantEntityListener.class})
public class FieldMapping extends BaseTenantEntity {
    // ID is now inherited from BaseEntity
    // No need for explicit ID field here

    @Column(name = "internal_field", length = 64)
    private String internalField;

    @Column(name = "external_field", length = 64)
    private String externalField;

    @Column(name = "transformation", length = 32)
    private String transformation;

    @Column(name = "is_enabled")
    private Boolean isEnabled;

    public String getInternalField() {
        return internalField;
    }

    public void setInternalField(String internalField) {
        this.internalField = internalField;
    }

    public String getExternalField() {
        return externalField;
    }

    public void setExternalField(String externalField) {
        this.externalField = externalField;
    }

    public String getTransformation() {
        return transformation;
    }

    public void setTransformation(String transformation) {
        this.transformation = transformation;
    }

    public Boolean getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(Boolean enabled) {
        isEnabled = enabled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false; // Compare parent class fields, including inherited ID
        FieldMapping that = (FieldMapping) o;
        return Objects.equals(internalField, that.internalField) &&
               Objects.equals(externalField, that.externalField) &&
               Objects.equals(transformation, that.transformation) &&
               Objects.equals(isEnabled, that.isEnabled);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), internalField, externalField, transformation, isEnabled);
    }
}
