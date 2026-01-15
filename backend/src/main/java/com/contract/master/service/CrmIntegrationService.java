package com.contract.master.service;

import com.contract.master.domain.ContractBase;
import com.contract.master.domain.ContractBaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Service
public class CrmIntegrationService {

    @Autowired
    private ContractBaseRepository contractBaseRepository;

    @Transactional
    public void syncContract(Map<String, Object> payload, String source) {
        String crmId = (String) payload.get("id");
        if (crmId == null) return;

        ContractBase contract = contractBaseRepository.findByCrmId(crmId)
                .orElse(new ContractBase());

        contract.setCrmId(crmId);
        contract.setCrmSource(source);
        contract.setContractName((String) payload.getOrDefault("name", "CRM Sync " + LocalDateTime.now()));
        contract.setContractNo((String) payload.getOrDefault("number", "SN-" + System.currentTimeMillis()));
        contract.setPartyAName((String) payload.get("party_a"));
        contract.setPartyBName((String) payload.get("party_b"));

        if (payload.containsKey("amount")) {
            contract.setAmount(new BigDecimal(payload.get("amount").toString()));
        }

        contract.setSyncTime(LocalDateTime.now());
        contract.setStatus("SYNCED");

        if (contract.getContractId() == null) {
            contract.setContractId(UUID.randomUUID().toString());
        }

        contractBaseRepository.save(contract);
    }
}
