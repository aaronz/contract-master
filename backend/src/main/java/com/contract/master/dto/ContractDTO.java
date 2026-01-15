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
    public String getContractStatus() { return contractStatus; }
    public void setContractStatus(String contractStatus) { this.contractStatus = contractStatus; }
    public String getTaxpayerId() { return taxpayerId; }
    public void setTaxpayerId(String taxpayerId) { this.taxpayerId = taxpayerId; }
    public Map<String, Object> getExtendedFields() { return extendedFields; }
    public void setExtendedFields(Map<String, Object> extendedFields) { this.extendedFields = extendedFields; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
    public String getCreateUser() { return createUser; }
    public void setCreateUser(String createUser) { this.createUser = createUser; }
    public LocalDateTime getUpdateTime() { return updateTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }
    public String getUpdateUser() { return updateUser; }
    public void setUpdateUser(String updateUser) { this.updateUser = updateUser; }
}
