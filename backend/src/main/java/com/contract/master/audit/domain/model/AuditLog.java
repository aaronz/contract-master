package com.contract.master.audit.domain.model;

import jakarta.persistence.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.contract.master.shared.domain.model.BaseTenantEntity;

import java.util.Objects;

@Entity
@Table(name = "audit_log")
@EntityListeners(AuditingEntityListener.class)
public class AuditLog extends BaseTenantEntity {
    // ID is now inherited from BaseEntity
    // No need for explicit ID field here

    @Column(name = "contract_id", length = 64)
    private String contractId;

    @Column(name = "field_name", length = 64)
    private String fieldName;

    @Column(name = "old_value", columnDefinition = "TEXT")
    private String oldValue;

    @Column(name = "new_value", columnDefinition = "TEXT")
    private String newValue;

    @Column(name = "modify_type", length = 32)
    private String modifyType;

    @Column(name = "modify_user", length = 64)
    private String modifyUser;

    @Column(name = "details", columnDefinition = "TEXT")
    private String details;

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getOldValue() {
        return oldValue;
    }

    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }

    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }

    public String getModifyType() {
        return modifyType;
    }

    public void setModifyType(String modifyType) {
        this.modifyType = modifyType;
    }

    public String getModifyUser() {
        return modifyUser;
    }

    public void setModifyUser(String modifyUser) {
        this.modifyUser = modifyUser;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false; // Compare parent class fields, including inherited ID
        AuditLog auditLog = (AuditLog) o;
        return Objects.equals(contractId, auditLog.contractId) &&
               Objects.equals(fieldName, auditLog.fieldName) &&
               Objects.equals(oldValue, auditLog.oldValue) &&
               Objects.equals(newValue, auditLog.newValue) &&
               Objects.equals(modifyType, auditLog.modifyType) &&
               Objects.equals(modifyUser, auditLog.modifyUser) &&
               Objects.equals(details, auditLog.details);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), contractId, fieldName, oldValue, newValue, modifyType, modifyUser, details);
    }
}
