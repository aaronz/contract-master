package com.contract.master.contract.domain.model;

import jakarta.persistence.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Objects;

import com.contract.master.shared.domain.base.BaseDomainEntity;

@Entity
@Table(name = "contract_extend_data")
@EntityListeners(AuditingEntityListener.class)
public class ContractExtendData extends BaseDomainEntity {

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
        ContractExtendData that = (ContractExtendData) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
