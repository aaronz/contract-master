package com.contract.master.controller;

import com.contract.master.entity.ContractBase;
import com.contract.master.repository.ContractBaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/webhook")
public class WebHookController {

    @Autowired
    private ContractBaseRepository contractBaseRepository;

    @PostMapping("/crm")
    public void receiveCrmSync(@RequestBody Map<String, Object> payload) {
        String crmId = (String) payload.get("crm_contract_id");
        if (crmId == null) return;
        
        ContractBase contract = contractBaseRepository.findByCrmContractId(crmId)
                .orElse(new ContractBase());
        
        contract.setCrmContractId(crmId);
        contract.setContractName((String) payload.getOrDefault("name", "Untitled Contract"));
        contract.setContractNo((String) payload.getOrDefault("contract_no", "SYNC-" + System.currentTimeMillis()));
        contract.setPartyAName((String) payload.get("party_a_name"));
        contract.setPartyBName((String) payload.get("party_b_name"));
        
        if (payload.containsKey("amount")) {
            contract.setContractAmount(new java.math.BigDecimal(payload.get("amount").toString()));
        }
        
        contract.setSyncTime(LocalDateTime.now());
        
        if (contract.getContractId() == null) {
            contract.setContractId(java.util.UUID.randomUUID().toString());
        }
        
        contractBaseRepository.save(contract);
    }
}
