package com.contract.master.contract.domain.model;

import jakarta.persistence.*;
import com.contract.master.shared.domain.model.BaseTenantEntity;

import java.util.Objects;

@Entity
@Table(name = "contract_change_record")
public class ContractChangeRecord extends BaseTenantEntity {
    @Column(name = "change_id", length = 64) // changeId is now a regular field
    private String changeId;

    @Column(name = "contract_id", length = 64)
    private String contractId;

    @Column(name = "change_type", length = 32)
    private String changeType;

    @Column(name = "change_content", columnDefinition = "TEXT")
    private String changeContent;

    @Column(name = "version_no")
    private Integer versionNo;

    public String getChangeId() {
        return changeId;
    }

    public void setChangeId(String changeId) {
        this.changeId = changeId;
    }

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    public String getChangeType() {
        return changeType;
    }

    public void setChangeType(String changeType) {
        this.changeType = changeType;
    }

    public String getChangeContent() {
        return changeContent;
    }

    public void setChangeContent(String changeContent) {
        this.changeContent = changeContent;
    }

    public Integer getVersionNo() {
        return versionNo;
    }

    public void setVersionNo(Integer versionNo) {
        this.versionNo = versionNo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false; // Compare parent class fields, including inherited ID
        ContractChangeRecord that = (ContractChangeRecord) o;
        return Objects.equals(changeId, that.changeId) &&
               Objects.equals(contractId, that.contractId) &&
               Objects.equals(changeType, that.changeType) &&
               Objects.equals(changeContent, that.changeContent) &&
               Objects.equals(versionNo, that.versionNo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), changeId, contractId, changeType, changeContent, versionNo);
    }
}
