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
        Page<ContractBase> basePage = contractBaseRepository.findByTenantId(tenantId, pageable);
        List<ContractDTO> dtoList = basePage.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return new org.springframework.data.domain.PageImpl<>(dtoList, pageable, basePage.getTotalElements());
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
    public ContractDTO createContract(ContractDTO dto) {
        ContractBase base = new ContractBase();
        base.setContractId(UUID.randomUUID().toString());
        base.setContractNo(dto.getContractNo());
        base.setContractName(dto.getContractName());
        base.setPartyAName(dto.getPartyAName());
        base.setPartyBName(dto.getPartyBName());
        base.setAmount(dto.getContractAmount());
        base.setStatus(dto.getContractStatus());
        base.setTenantId(TenantContext.getCurrentTenant());
        base.setCreateTime(LocalDateTime.now());
        base.setCreateUser("admin");
        
        ContractBase saved = contractBaseRepository.save(base);
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
        dto.setContractStatus(base.getStatus());

        dto.setCreateTime(base.getCreateTime());
        dto.setCreateUser(base.getCreateUser());
        dto.setUpdateTime(base.getUpdateTime());
        dto.setUpdateUser(base.getUpdateUser());

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
