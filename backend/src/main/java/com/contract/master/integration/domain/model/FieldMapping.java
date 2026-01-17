package com.contract.master.integration.domain.model;

import com.contract.master.shared.domain.model.BaseTenantEntity;
import com.contract.master.shared.infrastructure.persistence.TenantEntityListener;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "field_mapping")
@Data
@EqualsAndHashCode(callSuper = true)
@EntityListeners({TenantEntityListener.class})
public class FieldMapping extends BaseTenantEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "internal_field", length = 64)
    private String internalField;

    @Column(name = "external_field", length = 64)
    private String externalField;

    @Column(name = "transformation", length = 32)
    private String transformation;

    @Column(name = "is_enabled")
    private Boolean isEnabled;

    public void setInternalField(String internalField) { this.internalField = internalField; }
    public void setExternalField(String externalField) { this.externalField = externalField; }
}
