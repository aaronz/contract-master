package com.contract.master.integration.application;

import com.contract.master.contract.domain.model.ContractId;
import com.contract.master.shared.domain.model.TenantId;
import com.contract.master.contract.domain.model.Contract;
import com.contract.master.contract.domain.repository.ContractRepository;
import com.contract.master.evaluation.domain.service.RuleEngineDomainService;
import com.contract.master.security.TenantContext;
import com.contract.master.integration.domain.model.FieldMapping;
import com.contract.master.integration.domain.repository.FieldMappingRepository;
import com.contract.master.integration.domain.service.ScriptSandbox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class CrmIntegrationApplicationService {

    @Autowired
    private ContractRepository contractRepository;

    @Autowired
    private FieldMappingRepository mappingRepository;

    @Autowired
    private ScriptSandbox scriptSandbox;

    @Transactional
    public void syncContract(Map<String, Object> payload, String source) {
        String crmId = (String) payload.get("id");
        if (crmId == null) return;

        Contract contract = contractRepository.findById(ContractId.of(crmId))
                .orElseGet(() -> {
                    Contract newContract = new Contract();
                    newContract.setContractId(ContractId.of(UUID.randomUUID().toString()));
                    return newContract;
                });

        List<FieldMapping> mappings = mappingRepository.findByTargetSystemIdAndDirectionAndIsEnabledTrue(source, "INBOUND");
        for (FieldMapping mapping : mappings) {
            Object externalValue = payload.get(mapping.getExternalField());
            if (externalValue != null) {
                Object internalValue = applyInboundTransformation(externalValue, mapping);
                setContractField(contract, mapping.getInternalField(), internalValue);
            }
        }

        contract.syncFromCrm(
                crmId,
                source,
                contract.getContractName() != null ? contract.getContractName() : "CRM Sync " + LocalDateTime.now(),
                contract.getContractNo() != null ? contract.getContractNo().value() : "SN-" + System.currentTimeMillis()
        );

        contractRepository.save(contract);
    }

    private Object applyInboundTransformation(Object value, FieldMapping mapping) {
        if (mapping.getTransformationScript() != null && !mapping.getTransformationScript().trim().isEmpty()) {
            return scriptSandbox.execute(mapping.getTransformationScript(), value);
        }
        return value;
    }

    private void setContractField(Contract contract, String fieldName, Object value) {
        if (value == null) return;
        
        if ("contractName".equalsIgnoreCase(fieldName)) {
            contract.setContractName(value.toString());
        } else if ("amount".equalsIgnoreCase(fieldName)) {
            if (value instanceof Number) {
                contract.setAmount(new com.contract.master.contract.domain.model.ContractAmount(new java.math.BigDecimal(value.toString()), null, null, null, "USD"));
            }
        }
    }
}
