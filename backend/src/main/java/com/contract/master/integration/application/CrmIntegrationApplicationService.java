package com.contract.master.integration.application;

import com.contract.master.contract.domain.model.Contract;
import com.contract.master.contract.domain.repository.ContractRepository;
import com.contract.master.evaluation.application.RuleEngineService;
import com.contract.master.security.TenantContext;
import com.contract.master.dto.ContractDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.List;

@Service
public class CrmIntegrationApplicationService {

    @Autowired
    private ContractRepository contractRepository;

    @Autowired
    private RuleEngineService ruleEngine;

    @Transactional
    public void syncContract(Map<String, Object> payload, String source) {
        String crmId = (String) payload.get("id");
        if (crmId == null) return;

        Contract contract = contractRepository.findById(crmId)
                .orElseGet(() -> {
                    Contract newContract = new Contract();
                    newContract.setContractId(UUID.randomUUID().toString());
                    return newContract;
                });

        contract.syncFromCrm(
                crmId,
                source,
                (String) payload.getOrDefault("name", "CRM Sync " + LocalDateTime.now()),
                (String) payload.getOrDefault("number", "SN-" + System.currentTimeMillis())
        );

        contractRepository.save(contract);
    }
}
