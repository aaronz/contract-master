package com.contract.master.integration.domain.model;

import com.contract.master.shared.domain.model.BaseTenantEntity;
import com.contract.master.shared.infrastructure.persistence.TenantEntityListener;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "field_mapping")
@EntityListeners({TenantEntityListener.class})
public class FieldMapping extends BaseTenantEntity {

    @Column(name = "internal_field", length = 64)
    private String internalField;

    @Column(name = "external_field", length = 64)
    private String externalField;

    @Column(name = "transformation", length = 32)
    private String transformation;

    @Column(name = "transformation_script", columnDefinition = "TEXT")
    private String transformationScript;

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

    public String getTransformationScript() {
        return transformationScript;
    }

    public void setTransformationScript(String transformationScript) {
        this.transformationScript = transformationScript;
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
        if (!super.equals(o)) return false;
        FieldMapping that = (FieldMapping) o;
        return Objects.equals(internalField, that.internalField) &&
               Objects.equals(externalField, that.externalField) &&
               Objects.equals(transformation, that.transformation) &&
               Objects.equals(transformationScript, that.transformationScript) &&
               Objects.equals(isEnabled, that.isEnabled);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), internalField, externalField, transformation, transformationScript, isEnabled);
    }
}
