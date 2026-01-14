package com.contract.master.entity;

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
    @Id
    @Column(name = "contract_id", length = 64)
    private String contractId;

    @Column(name = "contract_no", length = 64)
    private String contractNo;

    @Column(name = "crm_contract_id", length = 64)
    private String crmContractId;

    @Column(name = "contract_type", length = 32)
    private String contractType;

    @Column(name = "contract_name", length = 255)
    private String contractName;

    @Column(name = "party_a_id", length = 64)
    private String partyAId;
    @Column(name = "party_a_name", length = 128)
    private String partyAName;
    @Column(name = "party_a_contact", length = 64)
    private String partyAContact;
    @Column(name = "party_a_phone", length = 20)
    private String partyAPhone;
    @Column(name = "party_a_address", length = 512)
    private String partyAAddress;

    @Column(name = "party_b_id", length = 64)
    private String partyBId;
    @Column(name = "party_b_name", length = 128)
    private String partyBName;
    @Column(name = "party_b_contact", length = 64)
    private String partyBContact;
    @Column(name = "party_b_phone", length = 20)
    private String partyBPhone;
    @Column(name = "party_b_address", length = 512)
    private String partyBAddress;

    @Column(name = "third_party_flag")
    private Boolean thirdPartyFlag;
    @Column(name = "third_party_info", columnDefinition = "TEXT")
    private String thirdPartyInfo;

    @Column(name = "subject_type", length = 32)
    private String subjectType;
    @Column(name = "subject_desc", columnDefinition = "TEXT")
    private String subjectDesc;
    @Column(name = "subject_quantity", precision = 18, scale = 4)
    private BigDecimal subjectQuantity;
    @Column(name = "unit_price", precision = 18, scale = 4)
    private BigDecimal unitPrice;

    @Column(name = "contract_amount", precision = 18, scale = 2)
    private BigDecimal contractAmount;
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

    @Column(name = "contract_status", length = 32)
    private String contractStatus;
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
}
