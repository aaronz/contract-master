package com.contract.master.service;

import com.contract.master.dto.AttachmentDTO;
import com.contract.master.dto.ContractDTO;
import com.contract.master.domain.ContractBase;
import com.contract.master.domain.ContractBaseRepository;
import com.contract.master.domain.*;
import com.contract.master.security.TenantContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ContractService {

    @Autowired
    private ContractBaseRepository contractBaseRepository;

    @Autowired
    private ContractExtendFieldRepository extendFieldRepository;

    @Autowired
    private ContractExtendDataRepository extendDataRepository;

    @Autowired
    private FieldConfigRepository fieldConfigRepository;

    @Autowired
    private ContractAttachmentRepository attachmentRepository;

    @Autowired
    private AuditService auditService;

    public List<ContractDTO> getAllContracts() {
        return contractBaseRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Page<ContractDTO> searchContracts(Pageable pageable) {
        String tenantId = TenantContext.getCurrentTenant();
        Pageable sortedPageable = org.springframework.data.domain.PageRequest.of(
            pageable.getPageNumber(), 
            pageable.getPageSize(), 
            org.springframework.data.domain.Sort.by("createTime").descending()
        );
        Page<ContractBase> basePage = contractBaseRepository.findByTenantId(tenantId, sortedPageable);
        List<ContractDTO> dtoList = basePage.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return new org.springframework.data.domain.PageImpl<>(dtoList, sortedPageable, basePage.getTotalElements());
    }

    public ContractDTO getContractById(String id) {
        return contractBaseRepository.findById(id)
                .map(this::convertToDTO)
                .orElse(null);
    }

    @Transactional
    public void updateContract(String id, ContractBase updated) {
        contractBaseRepository.findById(id).ifPresent(existing -> {
            auditService.logChange(id, "contract_name", existing.getContractName(), updated.getContractName(), "MANUAL", "admin");
            existing.setContractName(updated.getContractName());
            existing.setAmount(updated.getAmount());
            contractBaseRepository.save(existing);
        });
    }

    @Transactional
    public void updateContractFromDTO(String id, ContractDTO dto) {
        contractBaseRepository.findById(id).ifPresent(existing -> {
            auditService.logChange(id, "contract_base", "OLD_STATE", "UPDATED", "MANUAL", "admin");
            
            existing.setContractNo(dto.getContractNo());
            existing.setContractName(dto.getContractName());
            existing.setContractType(dto.getContractType());
            existing.setPartyAId(dto.getPartyAId());
            existing.setPartyAName(dto.getPartyAName());
            existing.setPartyBName(dto.getPartyBName());
            existing.setAmount(dto.getContractAmount());
            existing.setTaxRate(dto.getTaxRate());
            existing.setTaxAmount(dto.getTaxAmount());
            existing.setTotalAmountWithTax(dto.getTotalAmountWithTax());
            existing.setCurrencyType(dto.getCurrencyType());
            existing.setPaymentMethod(dto.getPaymentMethod());
            existing.setPaymentTerm(dto.getPaymentTerm());
            existing.setInvoiceType(dto.getInvoiceType());
            existing.setInvoiceTitle(dto.getInvoiceTitle());
            existing.setTaxpayerId(dto.getTaxpayerId());
            existing.setSignDate(dto.getSignDate());
            existing.setEffectiveDate(dto.getEffectiveDate());
            existing.setExpireDate(dto.getExpireDate());
            existing.setPerformanceLocation(dto.getPerformanceLocation());
            existing.setPerformanceMethod(dto.getPerformanceMethod());
            existing.setPerformanceStartDate(dto.getPerformanceStartDate());
            existing.setPerformanceEndDate(dto.getPerformanceEndDate());
            existing.setQualityStandard(dto.getQualityStandard());
            existing.setStatus(dto.getContractStatus());
            existing.setApprovalStatus(dto.getApprovalStatus());
            existing.setLegalReviewFlag(dto.getLegalReviewFlag());
            existing.setLegalReviewOpinion(dto.getLegalReviewOpinion());
            existing.setDisputeResolution(dto.getDisputeResolution());
            existing.setGoverningLaw(dto.getGoverningLaw());
            existing.setGuaranteeFlag(dto.getGuaranteeFlag());
            existing.setGuaranteeType(dto.getGuaranteeType());
            existing.setGuarantorInfo(dto.getGuarantorInfo());
            existing.setRemark(dto.getRemark());
            
            existing.setUpdateUser("admin");
            existing.setUpdateTime(LocalDateTime.now());
            
            contractBaseRepository.save(existing);
            
            if (dto.getExtendedFields() != null) {
                saveExtendedData(id, dto.getExtendedFields());
            }
        });
    }

    @Transactional
    public ContractDTO createContract(ContractDTO dto) {
        ContractBase base = new ContractBase();
        base.setContractId(UUID.randomUUID().toString());
        base.setContractNo(dto.getContractNo());
        base.setContractName(dto.getContractName());
        base.setContractType(dto.getContractType());
        base.setPartyAId(dto.getPartyAId());
        base.setPartyAName(dto.getPartyAName());
        base.setPartyBName(dto.getPartyBName());
        base.setAmount(dto.getContractAmount());
        base.setTaxRate(dto.getTaxRate());
        base.setTaxAmount(dto.getTaxAmount());
        base.setTotalAmountWithTax(dto.getTotalAmountWithTax());
        base.setCurrencyType(dto.getCurrencyType());
        base.setPaymentMethod(dto.getPaymentMethod());
        base.setPaymentTerm(dto.getPaymentTerm());
        base.setInvoiceType(dto.getInvoiceType());
        base.setInvoiceTitle(dto.getInvoiceTitle());
        base.setTaxpayerId(dto.getTaxpayerId());
        base.setSignDate(dto.getSignDate());
        base.setEffectiveDate(dto.getEffectiveDate());
        base.setExpireDate(dto.getExpireDate());
        base.setPerformanceLocation(dto.getPerformanceLocation());
        base.setPerformanceMethod(dto.getPerformanceMethod());
        base.setPerformanceStartDate(dto.getPerformanceStartDate());
        base.setPerformanceEndDate(dto.getPerformanceEndDate());
        base.setQualityStandard(dto.getQualityStandard());
        base.setStatus(dto.getContractStatus());
        base.setApprovalStatus(dto.getApprovalStatus());
        base.setLegalReviewFlag(dto.getLegalReviewFlag());
        base.setLegalReviewOpinion(dto.getLegalReviewOpinion());
        base.setDisputeResolution(dto.getDisputeResolution());
        base.setGoverningLaw(dto.getGoverningLaw());
        base.setGuaranteeFlag(dto.getGuaranteeFlag());
        base.setGuaranteeType(dto.getGuaranteeType());
        base.setGuarantorInfo(dto.getGuarantorInfo());
        base.setRemark(dto.getRemark());
        
        base.setTenantId(TenantContext.getCurrentTenant());
        base.setCreateTime(LocalDateTime.now());
        base.setCreateUser("admin");
        
        ContractBase saved = contractBaseRepository.save(base);
        
        if (dto.getExtendedFields() != null) {
            saveExtendedData(saved.getContractId(), dto.getExtendedFields());
        }
        
        auditService.logChange(saved.getContractId(), "contract_base", null, "CREATED", "MANUAL", "admin");
        return convertToDTO(saved);
    }

    @Transactional
    public void saveExtendedData(String contractId, Map<String, Object> data) {
        data.forEach((fieldCode, value) -> {
            extendFieldRepository.findAll().stream()
                .filter(f -> f.getFieldCode().equals(fieldCode))
                .findFirst()
                .ifPresent(field -> {
                    String newVal = value != null ? value.toString() : null;
                    boolean isVerifying = fieldCode.endsWith("_verified") && value instanceof Boolean && (Boolean)value;
                    String targetFieldCode = isVerifying ? fieldCode.replace("_verified", "") : fieldCode;

                    Optional<ContractExtendData> existing = extendDataRepository.findByContractId(contractId).stream()
                            .filter(d -> d.getFieldId().equals(field.getFieldId()))
                            .findFirst();

                    if (existing.isPresent()) {
                        if (isVerifying) {
                            existing.get().setVerificationStatus("VERIFIED");
                            extendDataRepository.save(existing.get());
                        } else if (!Objects.equals(existing.get().getFieldValue(), newVal)) {
                            auditService.logChange(contractId, fieldCode, existing.get().getFieldValue(), newVal, "MANUAL", "admin");
                            existing.get().setFieldValue(newVal);
                            existing.get().setFillType("MANUAL");
                            existing.get().setVerificationStatus("VERIFIED");
                            extendDataRepository.save(existing.get());
                        }
                    } else if (!isVerifying) {
                        auditService.logChange(contractId, fieldCode, null, newVal, "MANUAL", "admin");
                        ContractExtendData extendData = new ContractExtendData();
                        extendData.setContractId(contractId);
                        extendData.setFieldId(field.getFieldId());
                        extendData.setFieldValue(newVal);
                        extendData.setFillType("MANUAL");
                        extendData.setVerificationStatus("VERIFIED");
                        extendDataRepository.save(extendData);
                    }
                });
        });
    }

    private ContractDTO convertToDTO(ContractBase base) {
        ContractDTO dto = new ContractDTO();
        dto.setContractId(base.getContractId());
        dto.setContractNo(base.getContractNo());
        dto.setCrmContractId(base.getCrmId());
        dto.setContractName(base.getContractName());
        dto.setContractType(base.getContractType());
        
        dto.setPartyAId(base.getPartyAId());
        dto.setPartyAName(base.getPartyAName());
        dto.setPartyBName(base.getPartyBName());

        dto.setContractAmount(base.getAmount());
        dto.setTaxRate(base.getTaxRate());
        dto.setTaxAmount(base.getTaxAmount());
        dto.setTotalAmountWithTax(base.getTotalAmountWithTax());
        dto.setCurrencyType(base.getCurrencyType());
        dto.setPaymentMethod(base.getPaymentMethod());
        dto.setPaymentTerm(base.getPaymentTerm());
        
        dto.setInvoiceType(base.getInvoiceType());
        dto.setInvoiceTitle(base.getInvoiceTitle());
        dto.setTaxpayerId(base.getTaxpayerId());
        
        dto.setSignDate(base.getSignDate());
        dto.setEffectiveDate(base.getEffectiveDate());
        dto.setExpireDate(base.getExpireDate());
        
        dto.setPerformanceLocation(base.getPerformanceLocation());
        dto.setPerformanceMethod(base.getPerformanceMethod());
        dto.setPerformanceStartDate(base.getPerformanceStartDate());
        dto.setPerformanceEndDate(base.getPerformanceEndDate());
        dto.setQualityStandard(base.getQualityStandard());
        
        dto.setContractStatus(base.getStatus());
        dto.setApprovalStatus(base.getApprovalStatus());
        dto.setApprovalUser(base.getApprovalUser());
        
        dto.setLegalReviewFlag(base.getLegalReviewFlag());
        dto.setLegalReviewOpinion(base.getLegalReviewOpinion());
        dto.setDisputeResolution(base.getDisputeResolution());
        dto.setGoverningLaw(base.getGoverningLaw());
        dto.setGuaranteeFlag(base.getGuaranteeFlag());
        dto.setGuaranteeType(base.getGuaranteeType());
        dto.setGuarantorInfo(base.getGuarantorInfo());
        
        dto.setOwnerUserId(base.getOwnerUserId());
        dto.setOwnerDeptId(base.getOwnerDeptId());
        dto.setAttachmentCount(base.getAttachmentCount());
        dto.setMainAttachmentId(base.getMainAttachmentId());
        dto.setChangeCount(base.getChangeCount());
        dto.setLastChangeTime(base.getLastChangeTime());
        dto.setRemark(base.getRemark());

        dto.setCreateTime(base.getCreateTime());
        dto.setCreateUser(base.getCreateUser());
        dto.setUpdateTime(base.getUpdateTime());
        dto.setUpdateUser(base.getUpdateUser());
        dto.setTenantId(base.getTenantId());

        List<ContractExtendData> dataList = extendDataRepository.findByContractId(base.getContractId());
        Map<String, Object> extendedFields = new HashMap<>();
        for (ContractExtendData data : dataList) {
            extendFieldRepository.findById(data.getFieldId()).ifPresent(field -> {
                extendedFields.put(field.getFieldCode(), data.getFieldValue());
                extendedFields.put(field.getFieldCode() + "_source", data.getFillType());
                extendedFields.put(field.getFieldCode() + "_verified", "VERIFIED".equals(data.getVerificationStatus()));
            });
        }
        dto.setExtendedFields(extendedFields);

        return dto;
    }

    private boolean checkFieldRole(FieldConfig config) {
        if (config.getRequiredRole() == null) return true;
        Authentication auth = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        return auth != null && auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals(config.getRequiredRole()));
    }
}
