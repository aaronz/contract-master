package com.contract.master.contract.application;

import com.contract.master.contract.metadata.application.MetadataValidator;
import com.contract.master.contract.metadata.domain.event.FieldConfigChangedEvent;
import com.contract.master.contract.domain.event.ContractSavedEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import com.contract.master.contract.dto.AttachmentDTO;
import com.contract.master.contract.dto.ContractDTO;
import com.contract.master.contract.domain.model.*;
import com.contract.master.contract.domain.repository.*;
import com.contract.master.contract.metadata.domain.model.FieldConfig;
import com.contract.master.contract.metadata.domain.repository.FieldConfigRepository;
import com.contract.master.audit.application.AuditService;
import com.contract.master.shared.domain.model.*;
import com.contract.master.security.TenantContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ContractService {

    @Autowired
    private ContractRepository contractRepository;

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

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private MetadataValidator metadataValidator;

    private final Map<String, List<FieldConfig>> fieldConfigCache = new java.util.concurrent.ConcurrentHashMap<>();
    private static final String FIELD_CONFIG_TYPE_CONTRACT = "CONTRACT";

    @EventListener
    public void handleFieldConfigChanged(FieldConfigChangedEvent event) {
        fieldConfigCache.remove(event.getTenantId());
    }

    public List<ContractDTO> getAllContracts() {
        return contractRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void saveExtendedData(String contractId, Map<String, Object> data) {
        List<ContractExtendField> tenantExtendFields = extendFieldRepository.findAll();
        List<ContractExtendData> existingData = extendDataRepository.findByContractId(contractId);

        data.forEach((fieldCode, value) -> {
            tenantExtendFields.stream()
                .filter(f -> f.getFieldCode().equals(fieldCode))
                .findFirst()
                .ifPresent(field -> {
                    String newVal = value != null ? value.toString() : null;
                    
                    metadataValidator.validate(field, newVal);
                    
                    boolean isVerifying = fieldCode.endsWith("_verified") && value instanceof Boolean && (Boolean)value;
                    
                    Optional<ContractExtendData> existing = existingData.stream()
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

    public Page<ContractDTO> searchContracts(String query, Pageable pageable) {
        Pageable sortedPageable = org.springframework.data.domain.PageRequest.of(
            pageable.getPageNumber(), 
            pageable.getPageSize(), 
            org.springframework.data.domain.Sort.by("createTime").descending()
        );
        
        Page<Contract> basePage;
        if (query != null && !query.trim().isEmpty()) {
            basePage = contractRepository.search(query, sortedPageable);
        } else {
            basePage = contractRepository.findAll(sortedPageable);
        }
        
        List<ContractDTO> dtoList = basePage.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return new org.springframework.data.domain.PageImpl<>(dtoList, sortedPageable, basePage.getTotalElements());
    }

    public ContractDTO getContractById(String id) {
        return contractRepository.findById(ContractId.of(id))
                .map(this::convertToDTO)
                .orElse(null);
    }

    public Contract getRawContract(String id) {
        return contractRepository.findById(ContractId.of(id))
                .orElseThrow(() -> new RuntimeException("Contract not found: " + id));
    }

    @Transactional
    public void saveContract(Contract contract) {
        contractRepository.save(contract);
    }

    @Transactional
    public void updateContract(String id, Contract updated) {
        contractRepository.findById(ContractId.of(id)).ifPresent(existing -> {
            auditService.logChange(id, "contract_name", existing.getContractName(), updated.getContractName(), "MANUAL", "admin");
            existing.setContractName(updated.getContractName());
            existing.setAmount(updated.getAmount());
            contractRepository.save(existing);
            
            eventPublisher.publishEvent(new ContractSavedEvent(UUID.fromString(id), existing.getTenantId().getId()));
        });
    }

    @Transactional
    public void updateContractFromDTO(String id, ContractDTO dto) {
        contractRepository.findById(ContractId.of(id)).ifPresent(existing -> {
            auditService.logChange(id, "contract_base", "OLD_STATE", "UPDATED", "MANUAL", "admin");
            
            existing.setContractNo(new ContractNo(dto.getContractNo()));
            existing.setContractName(dto.getContractName());
            existing.setContractType(dto.getContractType());
            
            existing.setPartyA(new ContractParty(dto.getPartyAId(), dto.getPartyAName(), dto.getPartyAContact(), dto.getPartyAPhone(), dto.getPartyAAddress()));
            existing.setPartyB(new ContractParty(dto.getPartyBId(), dto.getPartyBName(), dto.getPartyBContact(), dto.getPartyBPhone(), dto.getPartyBAddress()));
            
            existing.setThirdPartyFlag(dto.getThirdPartyFlag());
            existing.setThirdPartyInfo(dto.getThirdPartyInfo());
            
            existing.setAmount(new ContractAmount(dto.getContractAmount(), dto.getTaxRate(), dto.getTaxAmount(), dto.getTotalAmountWithTax(), dto.getCurrencyType()));
            existing.setPayment(new ContractPayment(dto.getPaymentMethod(), dto.getPaymentTerm()));
            existing.setInvoice(new ContractInvoice(dto.getInvoiceType(), dto.getInvoiceTitle(), dto.getTaxpayerId()));
            
            existing.setTimeline(new ContractTimeline(dto.getSignDate(), dto.getEffectiveDate(), dto.getExpireDate(), dto.getPerformanceStartDate(), dto.getPerformanceEndDate()));
            
            existing.setPerformanceLocation(dto.getPerformanceLocation());
            existing.setPerformanceMethod(dto.getPerformanceMethod());
            existing.setQualityStandard(dto.getQualityStandard());
            
            existing.setStatus(dto.getContractStatus());
            existing.setApprovalStatus(dto.getApprovalStatus());
            
            existing.setCompliance(new ContractCompliance(dto.getLegalReviewFlag(), dto.getLegalReviewOpinion(), dto.getDisputeResolution(), dto.getGoverningLaw()));
            existing.setGuarantee(new ContractGuarantee(dto.getGuaranteeFlag(), dto.getGuaranteeType(), dto.getGuarantorInfo()));
            
            existing.setRemark(dto.getRemark());
            
            existing.setUpdateUser("admin");
            existing.setUpdateTime(LocalDateTime.now());
            
            contractRepository.save(existing);
            
            if (dto.getExtendedFields() != null) {
                saveExtendedData(id, dto.getExtendedFields());
            }

            eventPublisher.publishEvent(new ContractSavedEvent(UUID.fromString(id), existing.getTenantId().getId()));
        });
    }

    @Transactional
    public ContractDTO createContract(ContractDTO dto) {
        ContractId contractId;
        if (dto.getContractId() != null && !dto.getContractId().isEmpty()) {
            contractId = ContractId.of(dto.getContractId());
        } else {
            contractId = ContractId.generate();
        }
        
        ContractNo contractNo = new ContractNo(dto.getContractNo());
        
        Contract base = new Contract(contractId, null, contractNo);
        base.setContractName(dto.getContractName());
        base.setContractType(dto.getContractType());
        
        base.setPartyA(new ContractParty(dto.getPartyAId(), dto.getPartyAName(), dto.getPartyAContact(), dto.getPartyAPhone(), dto.getPartyAAddress()));
        base.setPartyB(new ContractParty(dto.getPartyBId(), dto.getPartyBName(), dto.getPartyBContact(), dto.getPartyBPhone(), dto.getPartyBAddress()));
        
        base.setThirdPartyFlag(dto.getThirdPartyFlag());
        base.setThirdPartyInfo(dto.getThirdPartyInfo());
        
        base.setAmount(new ContractAmount(dto.getContractAmount(), dto.getTaxRate(), dto.getTaxAmount(), dto.getTotalAmountWithTax(), dto.getCurrencyType()));
        base.setPayment(new ContractPayment(dto.getPaymentMethod(), dto.getPaymentTerm()));
        base.setInvoice(new ContractInvoice(dto.getInvoiceType(), dto.getInvoiceTitle(), dto.getTaxpayerId()));
        
        base.setTimeline(new ContractTimeline(dto.getSignDate(), dto.getEffectiveDate(), dto.getExpireDate(), dto.getPerformanceStartDate(), dto.getPerformanceEndDate()));
        
        base.setPerformanceLocation(dto.getPerformanceLocation());
        base.setPerformanceMethod(dto.getPerformanceMethod());
        base.setQualityStandard(dto.getQualityStandard());
        
        base.setStatus(dto.getContractStatus());
        base.setApprovalStatus(dto.getApprovalStatus());
        
        base.setCompliance(new ContractCompliance(dto.getLegalReviewFlag(), dto.getLegalReviewOpinion(), dto.getDisputeResolution(), dto.getGoverningLaw()));
        base.setGuarantee(new ContractGuarantee(dto.getGuaranteeFlag(), dto.getGuaranteeType(), dto.getGuarantorInfo()));
        
        base.setRemark(dto.getRemark());
        
        base.setCreateTime(LocalDateTime.now());
        base.setCreateUser("admin");
        
        Contract saved = contractRepository.save(base);
        
        if (dto.getExtendedFields() != null) {
            saveExtendedData(saved.getContractId().value().toString(), dto.getExtendedFields());
        }
        
        auditService.logChange(saved.getContractId().value().toString(), "contract", null, "CREATED", "MANUAL", "admin");
        
        eventPublisher.publishEvent(new ContractSavedEvent(saved.getContractId().value(), saved.getTenantId().getId()));

        return convertToDTO(saved);
    }

    public ContractDTO convertToDTO(Contract base) {
        ContractDTO dto = new ContractDTO();
        dto.setContractId(base.getContractId().value().toString());
        dto.setContractNo(base.getContractNo().value());
        dto.setCrmContractId(base.getCrmId());
        dto.setContractName(base.getContractName());
        dto.setContractType(base.getContractType());
        
        if (base.getPartyA() != null) {
            dto.setPartyAId(base.getPartyA().getPartyIdentifier());
            dto.setPartyAName(base.getPartyA().getName());
            dto.setPartyAContact(base.getPartyA().getContact());
            dto.setPartyAPhone(maskPhoneNumber(base.getPartyA().getPhone()));
            dto.setPartyAAddress(base.getPartyA().getAddress());
        }
        
        if (base.getPartyB() != null) {
            dto.setPartyBId(base.getPartyB().getPartyIdentifier());
            dto.setPartyBName(base.getPartyB().getName());
            dto.setPartyBContact(base.getPartyB().getContact());
            dto.setPartyBPhone(base.getPartyB().getPhone());
            dto.setPartyBAddress(base.getPartyB().getAddress());
        }

        dto.setThirdPartyFlag(base.getThirdPartyFlag());
        dto.setThirdPartyInfo(base.getThirdPartyInfo());

        if (base.getAmount() != null) {
            dto.setContractAmount(base.getAmount().getAmount());
            dto.setTaxRate(base.getAmount().getTaxRate());
            dto.setTaxAmount(base.getAmount().getTaxAmount());
            dto.setTotalAmountWithTax(base.getAmount().getTotalAmountWithTax());
            dto.setCurrencyType(base.getAmount().getCurrencyType());
        }
        
        if (base.getPayment() != null) {
            dto.setPaymentMethod(base.getPayment().getMethod());
            dto.setPaymentTerm(base.getPayment().getTerm());
        }
        
        if (base.getInvoice() != null) {
            dto.setInvoiceType(base.getInvoice().getType());
            dto.setInvoiceTitle(base.getInvoice().getTitle());
            dto.setTaxpayerId(base.getInvoice().getTaxpayerId());
        }
        
        if (base.getTimeline() != null) {
            dto.setSignDate(base.getTimeline().getSignDate());
            dto.setEffectiveDate(base.getTimeline().getEffectiveDate());
            dto.setExpireDate(base.getTimeline().getExpireDate());
            dto.setPerformanceStartDate(base.getTimeline().getPerformanceStartDate());
            dto.setPerformanceEndDate(base.getTimeline().getPerformanceEndDate());
        }
        
        dto.setPerformanceLocation(base.getPerformanceLocation());
        dto.setPerformanceMethod(base.getPerformanceMethod());
        dto.setQualityStandard(base.getQualityStandard());
        
        dto.setContractStatus(base.getStatus());
        dto.setApprovalStatus(base.getApprovalStatus());
        dto.setApprovalUser(base.getApprovalUser());
        
        if (base.getCompliance() != null) {
            dto.setLegalReviewFlag(base.getCompliance().getLegalReviewFlag());
            dto.setLegalReviewOpinion(base.getCompliance().getLegalReviewOpinion());
            dto.setDisputeResolution(base.getCompliance().getDisputeResolution());
            dto.setGoverningLaw(base.getCompliance().getGoverningLaw());
        }
        
        if (base.getGuarantee() != null) {
            dto.setGuaranteeFlag(base.getGuarantee().getFlag());
            dto.setGuaranteeType(base.getGuarantee().getType());
            dto.setGuarantorInfo(base.getGuarantee().getGuarantorInfo());
        }
        
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

        List<ContractExtendData> dataList = extendDataRepository.findByContractId(base.getContractId().value().toString());
        Map<String, Object> extendedFields = new HashMap<>();
        for (ContractExtendData data : dataList) {
            extendFieldRepository.findByFieldId(data.getFieldId()).ifPresent(field -> {
                extendedFields.put(field.getFieldCode(), data.getFieldValue());

                extendedFields.put(field.getFieldCode() + "_source", data.getFillType());
                extendedFields.put(field.getFieldCode() + "_verified", "VERIFIED".equals(data.getVerificationStatus()));
            });
        }
        dto.setExtendedFields(extendedFields);

        filterFields(dto);

        return dto;
    }

    private String maskPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.length() < 11) {
            return phoneNumber;
        }
        return phoneNumber.substring(0, 3) + "****" + phoneNumber.substring(7);
    }

    private void filterFields(ContractDTO dto) {
        TenantId tenantId = dto.getTenantId();
        String tenantIdKey = tenantId != null ? tenantId.toString() : "NONE";
        
        List<FieldConfig> configs = fieldConfigCache.computeIfAbsent(tenantIdKey, 
            tid -> fieldConfigRepository.findAll()
        );
        
        filterStandardFields(dto, configs);
        filterExtendedFields(dto, configs);
    }

    private void filterStandardFields(ContractDTO dto, List<FieldConfig> configs) {
        BeanWrapper src = new BeanWrapperImpl(dto);
        
        for (FieldConfig config : configs) {
            if (isFieldAccessible(config)) {
                continue;
            }
            
            String camelCaseField = snakeToCamelCase(config.getFieldCode());
            
            if (src.isWritableProperty(camelCaseField)) {
                src.setPropertyValue(camelCaseField, null);
            }
        }
    }

    private void filterExtendedFields(ContractDTO dto, List<FieldConfig> configs) {
        if (dto.getExtendedFields() == null || dto.getExtendedFields().isEmpty()) {
            return;
        }
        
        Map<String, Object> extendedFields = dto.getExtendedFields();
        Set<String> hiddenFieldCodes = configs.stream()
            .filter(config -> !isFieldAccessible(config))
            .map(FieldConfig::getFieldCode)
            .collect(Collectors.toSet());
        
        extendedFields.keySet().removeAll(hiddenFieldCodes);
    }

    private String snakeToCamelCase(String snakeCase) {
        if (snakeCase == null || snakeCase.isEmpty()) {
            return snakeCase;
        }
        
        StringBuilder result = new StringBuilder();
        boolean nextUpper = false;
        
        for (char c : snakeCase.toCharArray()) {
            if (c == '_') {
                nextUpper = true;
            } else {
                if (nextUpper) {
                    result.append(Character.toUpperCase(c));
                    nextUpper = false;
                } else {
                    result.append(Character.toLowerCase(c));
                }
            }
        }
        
        return result.toString();
    }

    private boolean isFieldAccessible(FieldConfig config) {
        if (config.getApiReturn() != null && !config.getApiReturn()) {
            return false;
        }
        return checkFieldRole(config);
    }

    public void clearFieldConfigCache() {
        fieldConfigCache.clear();
    }

    private boolean checkFieldRole(FieldConfig config) {
        if (config.getRequiredRole() == null) return true;
        Authentication auth = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        return auth != null && auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals(config.getRequiredRole()));
    }
}
