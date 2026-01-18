package com.contract.master.contract.domain.model;

import jakarta.persistence.*;
import com.contract.master.shared.domain.model.BaseTenantEntity;

import java.util.Objects;

@Entity
@Table(name = "contract_annotation")
public class ContractAnnotation extends BaseTenantEntity {
    // ID is now inherited from BaseEntity
    // No need for explicit ID field here

    @Column(name = "contract_id", length = 64)
    private String contractId;

    @Column(name = "user_id", length = 64)
    private String userId;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "status", length = 32)
    private String status;

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false; // Compare parent class fields, including inherited ID
        ContractAnnotation that = (ContractAnnotation) o;
        return Objects.equals(contractId, that.contractId) &&
               Objects.equals(userId, that.userId) &&
               Objects.equals(content, that.content) &&
               Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), contractId, userId, content, status);
    }
}
