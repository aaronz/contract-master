package com.contract.master.domain;

import com.contract.master.domain.BaseTenantEntity;
import com.contract.master.domain.TenantEntityListener;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "contract_base")
@Data
@EqualsAndHashCode(callSuper = true)
@EntityListeners({TenantEntityListener.class})
public class ContractBase extends BaseTenantEntity {
    @Override
    public String getTenantId() {
        return super.getTenantId();
    }

    @Override
    public void setTenantId(String tenantId) {
        super.setTenantId(tenantId);
    }
    @Id
    @Column(name = "contract_id", length = 64)
    private String contractId;

    @Column(name = "contract_no", length = 64)
    private String contractNo;

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

    @Column(name = "party_a_id", length = 64)
    private String partyAId;
    @Column(name = "party_a_name", length = 128)
    private String partyAName;

    @Column(name = "party_b_name", length = 128)
    private String partyBName;

    @Column(name = "amount", precision = 18, scale = 2)
    private BigDecimal amount;
    @Column(name = "tax_rate", precision = 5, scale = 2)
    private BigDecimal taxRate;
    @Column(name = "tax_amount", precision = 18, scale = 2)
    private BigDecimal taxAmount;
    @Column(name = "total_amount_with_tax", precision = 18, scale = 2)
    private BigDecimal totalAmountWithTax;
    @Column(name = "currency_type", length = 10)
    private String currencyType;
    @Column(name = "payment_method", length = 32)
    private String paymentMethod;
    @Column(name = "payment_term", columnDefinition = "TEXT")
    private String paymentTerm;

    @Column(name = "invoice_type", length = 32)
    private String invoiceType;
    @Column(name = "invoice_title", length = 128)
    private String invoiceTitle;
    @Column(name = "taxpayer_id", length = 64)
    private String taxpayerId;

    @Column(name = "sign_date")
    private LocalDate signDate;
    @Column(name = "effective_date")
    private LocalDate effectiveDate;
    @Column(name = "expire_date")
    private LocalDate expireDate;

    @Column(name = "performance_location", length = 512)
    private String performanceLocation;
    @Column(name = "performance_method", length = 64)
    private String performanceMethod;
    @Column(name = "performance_start_date")
    private LocalDate performanceStartDate;
    @Column(name = "performance_end_date")
    private LocalDate performanceEndDate;
    @Column(name = "quality_standard", columnDefinition = "TEXT")
    private String qualityStandard;

    @Column(name = "status", length = 32)
    private String status;
    @Column(name = "approval_status", length = 32)
    private String approvalStatus;
    @Column(name = "approval_user", length = 64)
    private String approvalUser;

    @Column(name = "legal_review_flag")
    private Boolean legalReviewFlag;
    @Column(name = "legal_review_opinion", columnDefinition = "TEXT")
    private String legalReviewOpinion;
    @Column(name = "dispute_resolution", length = 64)
    private String disputeResolution;
    @Column(name = "governing_law", length = 64)
    private String governingLaw;

    @Column(name = "guarantee_flag")
    private Boolean guaranteeFlag;
    @Column(name = "guarantee_type", length = 32)
    private String guaranteeType;
    @Column(name = "guarantor_info", columnDefinition = "TEXT")
    private String guarantorInfo;

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

    public String getContractId() { return contractId; }
    public void setContractId(String contractId) { this.contractId = contractId; }
    public String getContractNo() { return contractNo; }
    public void setContractNo(String contractNo) { this.contractNo = contractNo; }
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
    public String getPartyAId() { return partyAId; }
    public void setPartyAId(String partyAId) { this.partyAId = partyAId; }
    public String getPartyAName() { return partyAName; }
    public void setPartyAName(String partyAName) { this.partyAName = partyAName; }
    public String getPartyBName() { return partyBName; }
    public void setPartyBName(String partyBName) { this.partyBName = partyBName; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getSyncTime() { return syncTime; }
    public void setSyncTime(LocalDateTime syncTime) { this.syncTime = syncTime; }

    public BigDecimal getTaxRate() { return taxRate; }
    public BigDecimal getTaxAmount() { return taxAmount; }
    public BigDecimal getTotalAmountWithTax() { return totalAmountWithTax; }
    public String getCurrencyType() { return currencyType; }
    public String getPaymentMethod() { return paymentMethod; }
    public String getPaymentTerm() { return paymentTerm; }
    public String getInvoiceType() { return invoiceType; }
    public String getInvoiceTitle() { return invoiceTitle; }
    public String getTaxpayerId() { return taxpayerId; }
    public LocalDate getSignDate() { return signDate; }
    public LocalDate getEffectiveDate() { return effectiveDate; }
    public LocalDate getExpireDate() { return expireDate; }
    public String getPerformanceLocation() { return performanceLocation; }
    public String getPerformanceMethod() { return performanceMethod; }
    public LocalDate getPerformanceStartDate() { return performanceStartDate; }
    public LocalDate getPerformanceEndDate() { return performanceEndDate; }
    public String getQualityStandard() { return qualityStandard; }
    public String getApprovalStatus() { return approvalStatus; }
    public String getApprovalUser() { return approvalUser; }
    public Boolean getLegalReviewFlag() { return legalReviewFlag; }
    public String getLegalReviewOpinion() { return legalReviewOpinion; }
    public String getDisputeResolution() { return disputeResolution; }
    public String getGoverningLaw() { return governingLaw; }
    public Boolean getGuaranteeFlag() { return guaranteeFlag; }
    public String getGuaranteeType() { return guaranteeType; }
    public String getGuarantorInfo() { return guarantorInfo; }
    public Integer getAttachmentCount() { return attachmentCount; }
    public String getMainAttachmentId() { return mainAttachmentId; }
    public String getOwnerDeptId() { return ownerDeptId; }
    public String getOwnerUserId() { return ownerUserId; }
    public Integer getChangeCount() { return changeCount; }
    public LocalDateTime getLastChangeTime() { return lastChangeTime; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public Boolean getArchiveFlag() { return archiveFlag; }
    public LocalDateTime getArchiveTime() { return archiveTime; }
}
