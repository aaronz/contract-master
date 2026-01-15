package com.contract.master.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
public class ContractDTO {
    private String contractId;
    private String contractNo;
    private String crmContractId;
    private String contractType;
    private String contractName;

    private String partyAId;
    private String partyAName;
    private String partyAContact;
    @com.contract.master.security.DataMask(type = "PHONE")
    private String partyAPhone;
    private String partyAAddress;

    private String partyBId;
    private String partyBName;
    private String partyBContact;
    private String partyBPhone;
    private String partyBAddress;

    private BigDecimal contractAmount;
    private BigDecimal taxRate;
    private BigDecimal taxAmount;
    private BigDecimal totalAmountWithTax;
    private String currencyType;
    private String paymentMethod;
    private String paymentTerm;

    private String invoiceType;
    private String invoiceTitle;
    private String taxpayerId;

    private LocalDate signDate;
    private LocalDate effectiveDate;
    private LocalDate expireDate;

    private String performanceLocation;
    private String performanceMethod;
    private LocalDate performanceStartDate;
    private LocalDate performanceEndDate;
    private String qualityStandard;

    private String contractStatus;
    private String approvalStatus;
    private String approvalUser;
    private String ownerUserId;
    private String ownerDeptId;

    private Boolean legalReviewFlag;
    private String legalReviewOpinion;
    private String disputeResolution;
    private String governingLaw;
    private Boolean guaranteeFlag;
    private String guaranteeType;
    private String guarantorInfo;

    private String subjectType;
    private String subjectDesc;
    private BigDecimal subjectQuantity;
    private BigDecimal unitPrice;

    private Boolean thirdPartyFlag;
    private String thirdPartyInfo;

    private List<AttachmentDTO> attachments;
    private Integer attachmentCount;
    private String mainAttachmentId;

    private Integer changeCount;
    private LocalDateTime lastChangeTime;
    private String remark;
    private LocalDateTime createTime;
    private String createUser;
    private LocalDateTime updateTime;
    private String updateUser;
    private String tenantId;

    private Map<String, Object> extendedFields;

    public String getContractId() { return contractId; }
    public void setContractId(String contractId) { this.contractId = contractId; }
    public String getContractNo() { return contractNo; }
    public void setContractNo(String contractNo) { this.contractNo = contractNo; }
    public String getCrmContractId() { return crmContractId; }
    public void setCrmContractId(String crmContractId) { this.crmContractId = crmContractId; }
    public String getContractType() { return contractType; }
    public void setContractType(String contractType) { this.contractType = contractType; }
    public String getContractName() { return contractName; }
    public void setContractName(String contractName) { this.contractName = contractName; }
    public String getPartyAId() { return partyAId; }
    public void setPartyAId(String partyAId) { this.partyAId = partyAId; }
    public String getPartyAName() { return partyAName; }
    public void setPartyAName(String partyAName) { this.partyAName = partyAName; }
    public String getPartyAContact() { return partyAContact; }
    public void setPartyAContact(String partyAContact) { this.partyAContact = partyAContact; }
    public String getPartyAPhone() { return partyAPhone; }
    public void setPartyAPhone(String partyAPhone) { this.partyAPhone = partyAPhone; }
    public String getPartyAAddress() { return partyAAddress; }
    public void setPartyAAddress(String partyAAddress) { this.partyAAddress = partyAAddress; }
    public String getPartyBId() { return partyBId; }
    public void setPartyBId(String partyBId) { this.partyBId = partyBId; }
    public String getPartyBName() { return partyBName; }
    public void setPartyBName(String partyBName) { this.partyBName = partyBName; }
    public String getPartyBContact() { return partyBContact; }
    public void setPartyBContact(String partyBContact) { this.partyBContact = partyBContact; }
    public String getPartyBPhone() { return partyBPhone; }
    public void setPartyBPhone(String partyBPhone) { this.partyBPhone = partyBPhone; }
    public String getPartyBAddress() { return partyBAddress; }
    public void setPartyBAddress(String partyBAddress) { this.partyBAddress = partyBAddress; }
    public BigDecimal getContractAmount() { return contractAmount; }
    public void setContractAmount(BigDecimal contractAmount) { this.contractAmount = contractAmount; }
    public BigDecimal getTaxRate() { return taxRate; }
    public void setTaxRate(BigDecimal taxRate) { this.taxRate = taxRate; }
    public BigDecimal getTaxAmount() { return taxAmount; }
    public void setTaxAmount(BigDecimal taxAmount) { this.taxAmount = taxAmount; }
    public BigDecimal getTotalAmountWithTax() { return totalAmountWithTax; }
    public void setTotalAmountWithTax(BigDecimal totalAmountWithTax) { this.totalAmountWithTax = totalAmountWithTax; }
    public String getCurrencyType() { return currencyType; }
    public void setCurrencyType(String currencyType) { this.currencyType = currencyType; }
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    public String getPaymentTerm() { return paymentTerm; }
    public void setPaymentTerm(String paymentTerm) { this.paymentTerm = paymentTerm; }
    public String getInvoiceType() { return invoiceType; }
    public void setInvoiceType(String invoiceType) { this.invoiceType = invoiceType; }
    public String getInvoiceTitle() { return invoiceTitle; }
    public void setInvoiceTitle(String invoiceTitle) { this.invoiceTitle = invoiceTitle; }
    public String getTaxpayerId() { return taxpayerId; }
    public void setTaxpayerId(String taxpayerId) { this.taxpayerId = taxpayerId; }
    public LocalDate getSignDate() { return signDate; }
    public void setSignDate(LocalDate signDate) { this.signDate = signDate; }
    public LocalDate getEffectiveDate() { return effectiveDate; }
    public void setEffectiveDate(LocalDate effectiveDate) { this.effectiveDate = effectiveDate; }
    public LocalDate getExpireDate() { return expireDate; }
    public void setExpireDate(LocalDate expireDate) { this.expireDate = expireDate; }
    public String getPerformanceLocation() { return performanceLocation; }
    public void setPerformanceLocation(String performanceLocation) { this.performanceLocation = performanceLocation; }
    public String getPerformanceMethod() { return performanceMethod; }
    public void setPerformanceMethod(String performanceMethod) { this.performanceMethod = performanceMethod; }
    public LocalDate getPerformanceStartDate() { return performanceStartDate; }
    public void setPerformanceStartDate(LocalDate performanceStartDate) { this.performanceStartDate = performanceStartDate; }
    public LocalDate getPerformanceEndDate() { return performanceEndDate; }
    public void setPerformanceEndDate(LocalDate performanceEndDate) { this.performanceEndDate = performanceEndDate; }
    public String getQualityStandard() { return qualityStandard; }
    public void setQualityStandard(String qualityStandard) { this.qualityStandard = qualityStandard; }
    public String getContractStatus() { return contractStatus; }
    public void setContractStatus(String contractStatus) { this.contractStatus = contractStatus; }
    public String getApprovalStatus() { return approvalStatus; }
    public void setApprovalStatus(String approvalStatus) { this.approvalStatus = approvalStatus; }
    public String getApprovalUser() { return approvalUser; }
    public void setApprovalUser(String approvalUser) { this.approvalUser = approvalUser; }
    public String getOwnerUserId() { return ownerUserId; }
    public void setOwnerUserId(String ownerUserId) { this.ownerUserId = ownerUserId; }
    public String getOwnerDeptId() { return ownerDeptId; }
    public void setOwnerDeptId(String ownerDeptId) { this.ownerDeptId = ownerDeptId; }
    public Boolean getLegalReviewFlag() { return legalReviewFlag; }
    public void setLegalReviewFlag(Boolean legalReviewFlag) { this.legalReviewFlag = legalReviewFlag; }
    public String getLegalReviewOpinion() { return legalReviewOpinion; }
    public void setLegalReviewOpinion(String legalReviewOpinion) { this.legalReviewOpinion = legalReviewOpinion; }
    public String getDisputeResolution() { return disputeResolution; }
    public void setDisputeResolution(String disputeResolution) { this.disputeResolution = disputeResolution; }
    public String getGoverningLaw() { return governingLaw; }
    public void setGoverningLaw(String governingLaw) { this.governingLaw = governingLaw; }
    public Boolean getGuaranteeFlag() { return guaranteeFlag; }
    public void setGuaranteeFlag(Boolean guaranteeFlag) { this.guaranteeFlag = guaranteeFlag; }
    public String getGuaranteeType() { return guaranteeType; }
    public void setGuaranteeType(String guaranteeType) { this.guaranteeType = guaranteeType; }
    public String getGuarantorInfo() { return guarantorInfo; }
    public void setGuarantorInfo(String guarantorInfo) { this.guarantorInfo = guarantorInfo; }
    public String getSubjectType() { return subjectType; }
    public void setSubjectType(String subjectType) { this.subjectType = subjectType; }
    public String getSubjectDesc() { return subjectDesc; }
    public void setSubjectDesc(String subjectDesc) { this.subjectDesc = subjectDesc; }
    public BigDecimal getSubjectQuantity() { return subjectQuantity; }
    public void setSubjectQuantity(BigDecimal subjectQuantity) { this.subjectQuantity = subjectQuantity; }
    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }
    public Boolean getThirdPartyFlag() { return thirdPartyFlag; }
    public void setThirdPartyFlag(Boolean thirdPartyFlag) { this.thirdPartyFlag = thirdPartyFlag; }
    public String getThirdPartyInfo() { return thirdPartyInfo; }
    public void setThirdPartyInfo(String thirdPartyInfo) { this.thirdPartyInfo = thirdPartyInfo; }
    public List<AttachmentDTO> getAttachments() { return attachments; }
    public void setAttachments(List<AttachmentDTO> attachments) { this.attachments = attachments; }
    public Integer getAttachmentCount() { return attachmentCount; }
    public void setAttachmentCount(Integer attachmentCount) { this.attachmentCount = attachmentCount; }
    public String getMainAttachmentId() { return mainAttachmentId; }
    public void setMainAttachmentId(String mainAttachmentId) { this.mainAttachmentId = mainAttachmentId; }
    public Integer getChangeCount() { return changeCount; }
    public void setChangeCount(Integer changeCount) { this.changeCount = changeCount; }
    public LocalDateTime getLastChangeTime() { return lastChangeTime; }
    public void setLastChangeTime(LocalDateTime lastChangeTime) { this.lastChangeTime = lastChangeTime; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
    public String getCreateUser() { return createUser; }
    public void setCreateUser(String createUser) { this.createUser = createUser; }
    public LocalDateTime getUpdateTime() { return updateTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }
    public String getUpdateUser() { return updateUser; }
    public void setUpdateUser(String updateUser) { this.updateUser = updateUser; }
    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    public Map<String, Object> getExtendedFields() { return extendedFields; }
    public void setExtendedFields(Map<String, Object> extendedFields) { this.extendedFields = extendedFields; }
}
