package com.contract.master.contract.domain.model;

import com.contract.master.shared.domain.model.BaseTenantEntity;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "contract_extend_data")
public class ContractExtendData extends BaseTenantEntity {

    public ContractExtendData() {
        
    }

    @Column(name = "contract_id", length = 64)
    private String contractId;

    @Column(name = "field_id", length = 64)
    private String fieldId;

    @Column(name = "field_value", columnDefinition = "TEXT")
    private String fieldValue;

    @Column(name = "fill_type", length = 32)
    private String fillType;

    @Column(name = "fill_user", length = 64)
    private String fillUser;

    @Column(name = "fill_time")
    private LocalDateTime fillTime;

    @Column(name = "verification_status", length = 32)
    private String verificationStatus;

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    public String getFieldId() {
        return fieldId;
    }

    public void setFieldId(String fieldId) {
        this.fieldId = fieldId;
    }

    public String getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(String fieldValue) {
        this.fieldValue = fieldValue;
    }

    public String getFillType() {
        return fillType;
    }

    public void setFillType(String fillType) {
        this.fillType = fillType;
    }

    public String getFillUser() {
        return fillUser;
    }

    public void setFillUser(String fillUser) {
        this.fillUser = fillUser;
    }

    public LocalDateTime getFillTime() {
        return fillTime;
    }

    public void setFillTime(LocalDateTime fillTime) {
        this.fillTime = fillTime;
    }

    public String getVerificationStatus() {
        return verificationStatus;
    }

    public void setVerificationStatus(String verificationStatus) {
        this.verificationStatus = verificationStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ContractExtendData that = (ContractExtendData) o;
        return Objects.equals(contractId, that.contractId) &&
               Objects.equals(fieldId, that.fieldId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), contractId, fieldId);
    }
}
