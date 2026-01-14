package com.contract.master.service;

import com.contract.master.dto.AttachmentDTO;
import com.contract.master.dto.ContractDTO;
import com.contract.master.entity.*;
import com.contract.master.repository.*;
import com.contract.master.security.TenantContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
            existing.setContractAmount(updated.getContractAmount());
            contractBaseRepository.save(existing);
        });
    }

    @Transactional
    public void saveExtendedData(String contractId, Map<String, Object> data) {
        data.forEach((fieldCode, value) -> {
            extendFieldRepository.findAll().stream()
                .filter(f -> f.getFieldCode().equals(fieldCode))
                .findFirst()
                .ifPresent(field -> {
                    String newVal = value != null ? value.toString() : null;
                    
                    Optional<ContractExtendData> existing = extendDataRepository.findByContractId(contractId).stream()
                            .filter(d -> d.getFieldId().equals(field.getFieldId()))
                            .findFirst();

                    if (existing.isPresent()) {
                        if (!Objects.equals(existing.get().getFieldValue(), newVal)) {
                            auditService.logChange(contractId, fieldCode, existing.get().getFieldValue(), newVal, "MANUAL", "admin");
                            existing.get().setFieldValue(newVal);
                            extendDataRepository.save(existing.get());
                        }
                    } else {
                        auditService.logChange(contractId, fieldCode, null, newVal, "MANUAL", "admin");
                        ContractExtendData extendData = new ContractExtendData();
                        extendData.setContractId(contractId);
                        extendData.setFieldId(field.getFieldId());
                        extendData.setFieldValue(newVal);
                        extendData.setFillType("MANUAL");
                        extendDataRepository.save(extendData);
                    }
                });
        });
    }

    private ContractDTO convertToDTO(ContractBase base) {
        ContractDTO dto = new ContractDTO();
        dto.setContractId(base.getContractId());
        dto.setContractNo(base.getContractNo());
        dto.setCrmContractId(base.getCrmContractId());
        dto.setContractName(base.getContractName());
        dto.setContractType(base.getContractType());
        
        dto.setPartyAId(base.getPartyAId());
        dto.setPartyAName(base.getPartyAName());
        dto.setPartyAContact(base.getPartyAContact());
        dto.setPartyAPhone(base.getPartyAPhone());
        dto.setPartyAAddress(base.getPartyAAddress());

        dto.setPartyBId(base.getPartyBId());
        dto.setPartyBName(base.getPartyBName());
        dto.setPartyBContact(base.getPartyBContact());
        dto.setPartyBPhone(base.getPartyBPhone());
        dto.setPartyBAddress(base.getPartyBAddress());

        dto.setContractAmount(base.getContractAmount());
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

        dto.setContractStatus(base.getContractStatus());
        dto.setApprovalStatus(base.getApprovalStatus());
        dto.setApprovalUser(base.getApprovalUser());
        dto.setOwnerUserId(base.getOwnerUserId());
        dto.setOwnerDeptId(base.getOwnerDeptId());

        dto.setLegalReviewFlag(base.getLegalReviewFlag());
        dto.setLegalReviewOpinion(base.getLegalReviewOpinion());
        dto.setDisputeResolution(base.getDisputeResolution());
        dto.setGoverningLaw(base.getGoverningLaw());
        dto.setGuaranteeFlag(base.getGuaranteeFlag());
        dto.setGuaranteeType(base.getGuaranteeType());
        dto.setGuarantorInfo(base.getGuarantorInfo());

        dto.setSubjectType(base.getSubjectType());
        dto.setSubjectDesc(base.getSubjectDesc());
        dto.setSubjectQuantity(base.getSubjectQuantity());
        dto.setUnitPrice(base.getUnitPrice());

        dto.setThirdPartyFlag(base.getThirdPartyFlag());
        dto.setThirdPartyInfo(base.getThirdPartyInfo());

        dto.setAttachmentCount(base.getAttachmentCount());
        dto.setMainAttachmentId(base.getMainAttachmentId());
        dto.setChangeCount(base.getChangeCount());
        dto.setLastChangeTime(base.getLastChangeTime());
        dto.setRemark(base.getRemark());
        
        dto.setCreateTime(base.getCreateTime());
        dto.setCreateUser(base.getCreateUser());
        dto.setUpdateTime(base.getUpdateTime());
        dto.setUpdateUser(base.getUpdateUser());

        List<ContractExtendData> dataList = extendDataRepository.findByContractId(base.getContractId());
        Map<String, Object> extendedFields = new HashMap<>();
        for (ContractExtendData data : dataList) {
            extendFieldRepository.findById(data.getFieldId()).ifPresent(field -> {
                extendedFields.put(field.getFieldCode(), data.getFieldValue());
            });
        }
        
        List<FieldConfig> configs = fieldConfigRepository.findByTenantId(TenantContext.getCurrentTenant());
        if (!configs.isEmpty()) {
            Map<String, Object> filtered = new HashMap<>();
            configs.stream().filter(c -> "API_RETURN".equals(c.getConfigType()) && c.getIsVisible())
                    .forEach(c -> {
                        if (extendedFields.containsKey(c.getFieldCode())) {
                            if (checkFieldRole(c)) {
                                filtered.put(c.getFieldAlias() != null ? c.getFieldAlias() : c.getFieldCode(), extendedFields.get(c.getFieldCode()));
                            }
                        }
                    });
            dto.setExtendedFields(filtered);

            // Filter base fields based on config
            configs.stream().filter(c -> !"API_RETURN".equals(c.getConfigType()) && !c.getIsVisible())
                    .forEach(c -> {
                        if ("contractAmount".equals(c.getFieldCode())) dto.setContractAmount(null);
                        if ("taxpayerId".equals(c.getFieldCode())) dto.setTaxpayerId(null);
                    });
        } else {
            dto.setExtendedFields(extendedFields);
        }

        return dto;
    }

    private boolean checkFieldRole(FieldConfig config) {
        if (config.getRequiredRole() == null) return true;
        Authentication auth = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        return auth != null && auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals(config.getRequiredRole()));
    }
}

