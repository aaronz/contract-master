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
    private String partyAPhone;
    private String partyAAddress;

    @com.contract.master.security.DataMask(type = "PHONE")
    private String partyAPhone;

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
}
