package com.contract.master.contract.domain.model;

import com.contract.master.domain.BaseTenantEntity;
import com.contract.master.domain.TenantEntityListener;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "contract_base")
@EntityListeners({TenantEntityListener.class})
public class Contract extends BaseTenantEntity {
    @Id
    @Column(name = "contract_id", length = 64)
    private String contractId;

    @Column(name = "contract_no", length = 64)
    private String contractNo;

    @Column(name = "crm_id", length = 64)
    private String crmId;

    @Column(name = "crm_source", length = 32)
    private String crmSource;

    @Column(name = "contract_type", length = 32)
    private String contractType;

    @Column(name = "contract_name", length = 255)
    private String contractName;

    public void setCrmId(String crmId) { this.crmId = crmId; }
    public void setCrmSource(String crmSource) { this.crmSource = crmSource; }
    public void setContractName(String contractName) { this.contractName = contractName; }
    public void setSyncTime(LocalDateTime syncTime) { this.syncTime = syncTime; }

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "id", column = @Column(name = "party_a_id")),
        @AttributeOverride(name = "name", column = @Column(name = "party_a_name")),
        @AttributeOverride(name = "contact", column = @Column(name = "party_a_contact")),
        @AttributeOverride(name = "phone", column = @Column(name = "party_a_phone")),
        @AttributeOverride(name = "address", column = @Column(name = "party_a_address"))
    })
    private ContractParty partyA;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "id", column = @Column(name = "party_b_id")),
        @AttributeOverride(name = "name", column = @Column(name = "party_b_name")),
        @AttributeOverride(name = "contact", column = @Column(name = "party_b_contact")),
        @AttributeOverride(name = "phone", column = @Column(name = "party_b_phone")),
        @AttributeOverride(name = "address", column = @Column(name = "party_b_address"))
    })
    private ContractParty partyB;

    @Embedded
    private ContractAmount amount;

    @Embedded
    private ContractTimeline timeline;

    @Column(name = "status", length = 32)
    private String status;

    @Column(name = "sync_time")
    private LocalDateTime syncTime;

    @Column(name = "archive_flag")
    private Boolean archiveFlag;

    @Column(name = "archive_time")
    private LocalDateTime archiveTime;

    public String getContractId() { return contractId; }
    public void setContractId(String contractId) { this.contractId = contractId; }
    public String getContractNo() { return contractNo; }
    public void setContractNo(String contractNo) { this.contractNo = contractNo; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Boolean getArchiveFlag() { return archiveFlag; }
    public void setArchiveFlag(Boolean archiveFlag) { this.archiveFlag = archiveFlag; }
    public LocalDateTime getArchiveTime() { return archiveTime; }
    public void setArchiveTime(LocalDateTime archiveTime) { this.archiveTime = archiveTime; }

    public void verify() {
        if ("PUBLISHED".equals(this.status)) {
            throw new IllegalStateException("Already published");
        }
        this.status = "VERIFIED";
    }

    public void archive() {
        this.archiveFlag = true;
        this.archiveTime = LocalDateTime.now();
        this.status = "ARCHIVED";
    }
}
