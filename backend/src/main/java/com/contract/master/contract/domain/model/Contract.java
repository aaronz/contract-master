package com.contract.master.contract.domain.model;

import com.contract.master.shared.domain.base.AggregateRoot;
import com.contract.master.shared.domain.base.BaseDomainEntity;
import com.contract.master.shared.domain.model.BaseTenantEntity;
import com.contract.master.shared.domain.model.Money;
import com.contract.master.shared.domain.model.PartyInfo;
import com.contract.master.shared.domain.model.TenantId;
import com.contract.master.shared.infrastructure.persistence.TenantEntityListener;
import com.contract.master.constant.ContractStatus;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "contract_base")
@EntityListeners({TenantEntityListener.class})
public class Contract extends BaseTenantEntity implements AggregateRoot {
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "contract_id", length = 64))
    private ContractId contractId;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "contract_no", length = 64))
    private ContractNo contractNo;

    public Contract() {
        
    }

    public Contract(ContractId contractId, TenantId tenantId, ContractNo contractNo) {
        
        this.contractId = Objects.requireNonNull(contractId);
        setTenantId(tenantId);
        this.contractNo = Objects.requireNonNull(contractNo);
        this.status = "DRAFT";
        this.changeCount = 0;
    }

    public void sign(LocalDate date) {
        this.status = "SIGNED";
    }

    public void activate(LocalDate date) {
        this.status = "ACTIVE";
    }

    public void terminate(String reason) {
        this.status = "TERMINATED";
    }

    @Column(name = "crm_id", length = 64)
    private String crmId;

    @Column(name = "crm_source", length = 32)
    private String crmSource;

    @Column(name = "group_id", length = 64)
    private String groupId;

    @Column(name = "contract_type", length = 32)
    private String contractType;

    @Column(name = "contract_name", length = 255)
    private String contractName;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "partyIdentifier", column = @Column(name = "party_a_id")),
        @AttributeOverride(name = "name", column = @Column(name = "party_a_name")),
        @AttributeOverride(name = "contact", column = @Column(name = "party_a_contact")),
        @AttributeOverride(name = "phone", column = @Column(name = "party_a_phone")),
        @AttributeOverride(name = "address", column = @Column(name = "party_a_address"))
    })
    private ContractParty partyA;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "partyIdentifier", column = @Column(name = "party_b_id")),
        @AttributeOverride(name = "name", column = @Column(name = "party_b_name")),
        @AttributeOverride(name = "contact", column = @Column(name = "party_b_contact")),
        @AttributeOverride(name = "phone", column = @Column(name = "party_b_phone")),
        @AttributeOverride(name = "address", column = @Column(name = "party_b_address"))
    })
    private ContractParty partyB;

    @Column(name = "third_party_flag")
    private Boolean thirdPartyFlag;
    @Column(name = "third_party_info", columnDefinition = "TEXT")
    private String thirdPartyInfo;

    @Embedded
    private ContractAmount amount;

    @Embedded
    private ContractPayment payment;

    @Embedded
    private ContractInvoice invoice;

    @Embedded
    private ContractTimeline timeline;

    @Column(name = "performance_location", length = 512)
    private String performanceLocation;
    @Column(name = "performance_method", length = 64)
    private String performanceMethod;
    @Column(name = "quality_standard", columnDefinition = "TEXT")
    private String qualityStandard;

    @Column(name = "status", length = 32)
    private String status;

    @Column(name = "approval_status", length = 32)
    private String approvalStatus;
    @Column(name = "approval_user", length = 64)
    private String approvalUser;

    @Embedded
    private ContractCompliance compliance;

    @Embedded
    private ContractGuarantee guarantee;

    @Column(name = "attachment_count")
    private Integer attachmentCount;
    @Column(name = "main_attachment_id", length = 64)
    private String mainAttachmentId;
    @Column(name = "owner_dept_id", length = 64)
    private String ownerDeptId;
    @Column(name = "owner_user_id", length = 64)
    private String ownerUserId;
    @Column(name = "change_count")
    private Integer changeCount;
    @Column(name = "last_change_time")
    private LocalDateTime lastChangeTime;
    @Column(name = "remark", columnDefinition = "TEXT")
    private String remark;
    @Column(name = "sync_time")
    private LocalDateTime syncTime;

    @Column(name = "archive_flag")
    private Boolean archiveFlag;
    @Column(name = "archive_time")
    private LocalDateTime archiveTime;

    @Column(name = "validation_results", columnDefinition = "TEXT")
    private String validationResults;

    // Explicit Getters and Setters to avoid Lombok issues in some environments
    public TenantId getTenantId() { return super.getTenantId(); }
    public void setTenantId(TenantId tenantId) { super.setTenantId(tenantId); }
    public ContractId getContractId() { return contractId; }
    public void setContractId(ContractId contractId) { this.contractId = contractId; }
    public ContractNo getContractNo() { return contractNo; }
    public void setContractNo(ContractNo contractNo) { this.contractNo = contractNo; }
    public String getCrmId() { return crmId; }
    public void setCrmId(String crmId) { this.crmId = crmId; }
    public String getCrmSource() { return crmSource; }
    public void setCrmSource(String crmSource) { this.crmSource = crmSource; }
    public String getGroupId() { return groupId; }
    public void setGroupId(String groupId) { this.groupId = groupId; }
    public String getContractType() { return contractType; }
    public void setContractType(String contractType) { this.contractType = contractType; }
    public String getContractName() { return contractName; }
    public void setContractName(String contractName) { this.contractName = contractName; }
    public ContractParty getPartyA() { return partyA; }
    public void setPartyA(ContractParty partyA) { this.partyA = partyA; }
    public ContractParty getPartyB() { return partyB; }
    public void setPartyB(ContractParty partyB) { this.partyB = partyB; }
    public Boolean getThirdPartyFlag() { return thirdPartyFlag; }
    public void setThirdPartyFlag(Boolean thirdPartyFlag) { this.thirdPartyFlag = thirdPartyFlag; }
    public String getThirdPartyInfo() { return thirdPartyInfo; }
    public void setThirdPartyInfo(String thirdPartyInfo) { this.thirdPartyInfo = thirdPartyInfo; }
    public ContractAmount getAmount() { return amount; }
    public void setAmount(ContractAmount amount) { this.amount = amount; }
    public ContractPayment getPayment() { return payment; }
    public void setPayment(ContractPayment payment) { this.payment = payment; }
    public ContractInvoice getInvoice() { return invoice; }
    public void setInvoice(ContractInvoice invoice) { this.invoice = invoice; }
    public ContractTimeline getTimeline() { return timeline; }
    public void setTimeline(ContractTimeline timeline) { this.timeline = timeline; }
    public String getPerformanceLocation() { return performanceLocation; }
    public void setPerformanceLocation(String performanceLocation) { this.performanceLocation = performanceLocation; }
    public String getPerformanceMethod() { return performanceMethod; }
    public void setPerformanceMethod(String performanceMethod) { this.performanceMethod = performanceMethod; }
    public String getQualityStandard() { return qualityStandard; }
    public void setQualityStandard(String qualityStandard) { this.qualityStandard = qualityStandard; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getApprovalStatus() { return approvalStatus; }
    public void setApprovalStatus(String approvalStatus) { this.approvalStatus = approvalStatus; }
    public String getApprovalUser() { return approvalUser; }
    public void setApprovalUser(String approvalUser) { this.approvalUser = approvalUser; }
    public ContractCompliance getCompliance() { return compliance; }
    public void setCompliance(ContractCompliance compliance) { this.compliance = compliance; }
    public ContractGuarantee getGuarantee() { return guarantee; }
    public void setGuarantee(ContractGuarantee guarantee) { this.guarantee = guarantee; }
    public Integer getAttachmentCount() { return attachmentCount; }
    public void setAttachmentCount(Integer attachmentCount) { this.attachmentCount = attachmentCount; }
    public String getMainAttachmentId() { return mainAttachmentId; }
    public void setMainAttachmentId(String mainAttachmentId) { this.mainAttachmentId = mainAttachmentId; }
    public String getOwnerDeptId() { return ownerDeptId; }
    public void setOwnerDeptId(String ownerDeptId) { this.ownerDeptId = ownerDeptId; }
    public String getOwnerUserId() { return ownerUserId; }
    public void setOwnerUserId(String ownerUserId) { this.ownerUserId = ownerUserId; }
    public Integer getChangeCount() { return changeCount; }
    public void setChangeCount(Integer changeCount) { this.changeCount = changeCount; }
    public LocalDateTime getLastChangeTime() { return lastChangeTime; }
    public void setLastChangeTime(LocalDateTime lastChangeTime) { this.lastChangeTime = lastChangeTime; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public LocalDateTime getSyncTime() { return syncTime; }
    public void setSyncTime(LocalDateTime syncTime) { this.syncTime = syncTime; }
    public Boolean getArchiveFlag() { return archiveFlag; }
    public void setArchiveFlag(Boolean archiveFlag) { this.archiveFlag = archiveFlag; }
    public LocalDateTime getArchiveTime() { return archiveTime; }
    public void setArchiveTime(LocalDateTime archiveTime) { this.archiveTime = archiveTime; }
    public String getValidationResults() { return validationResults; }
    public void setValidationResults(String validationResults) { this.validationResults = validationResults; }

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

    public void syncFromCrm(String crmId, String source, String name, String number) {
        this.crmId = crmId;
        this.crmSource = source;
        this.contractName = name;
        this.contractNo = new ContractNo(number);
        this.syncTime = LocalDateTime.now();
        if (this.status == null) {
            this.status = "SYNCED";
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false; // Compare parent class fields, including inherited ID
        Contract contract = (Contract) o;
        return Objects.equals(contractId, contract.contractId) &&
               Objects.equals(contractNo, contract.contractNo) &&
               Objects.equals(crmId, contract.crmId) &&
               Objects.equals(crmSource, contract.crmSource) &&
               Objects.equals(groupId, contract.groupId) &&
               Objects.equals(contractType, contract.contractType) &&
               Objects.equals(contractName, contract.contractName) &&
               Objects.equals(partyA, contract.partyA) &&
               Objects.equals(partyB, contract.partyB) &&
               Objects.equals(thirdPartyFlag, contract.thirdPartyFlag) &&
               Objects.equals(thirdPartyInfo, contract.thirdPartyInfo) &&
               Objects.equals(amount, contract.amount) &&
               Objects.equals(payment, contract.payment) &&
               Objects.equals(invoice, contract.invoice) &&
               Objects.equals(timeline, contract.timeline) &&
               Objects.equals(performanceLocation, contract.performanceLocation) &&
               Objects.equals(performanceMethod, contract.performanceMethod) &&
               Objects.equals(qualityStandard, contract.qualityStandard) &&
               Objects.equals(status, contract.status) &&
               Objects.equals(approvalStatus, contract.approvalStatus) &&
               Objects.equals(approvalUser, contract.approvalUser) &&
               Objects.equals(compliance, contract.compliance) &&
               Objects.equals(guarantee, contract.guarantee) &&
               Objects.equals(attachmentCount, contract.attachmentCount) &&
               Objects.equals(mainAttachmentId, contract.mainAttachmentId) &&
               Objects.equals(ownerDeptId, contract.ownerDeptId) &&
               Objects.equals(ownerUserId, contract.ownerUserId) &&
               Objects.equals(changeCount, contract.changeCount) &&
               Objects.equals(lastChangeTime, contract.lastChangeTime) &&
               Objects.equals(remark, contract.remark) &&
               Objects.equals(syncTime, contract.syncTime) &&
               Objects.equals(archiveFlag, contract.archiveFlag) &&
               Objects.equals(archiveTime, contract.archiveTime) &&
               Objects.equals(validationResults, contract.validationResults);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), contractId, contractNo, crmId, crmSource, groupId, contractType, contractName, partyA, partyB, thirdPartyFlag, thirdPartyInfo, amount, payment, invoice, timeline, performanceLocation, performanceMethod, qualityStandard, status, approvalStatus, approvalUser, compliance, guarantee, attachmentCount, mainAttachmentId, ownerDeptId, ownerUserId, changeCount, lastChangeTime, remark, syncTime, archiveFlag, archiveTime, validationResults);
    }
}
