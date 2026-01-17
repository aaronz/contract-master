package com.contract.master.contract.application;

import com.contract.master.contract.domain.model.Contract;
import com.contract.master.contract.domain.repository.ContractRepository;
import com.contract.master.service.AuditService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ContractApplicationService {
    private final ContractRepository contractRepository;
    private final AuditService auditService;

    public ContractApplicationService(ContractRepository contractRepository, AuditService auditService) {
        this.contractRepository = contractRepository;
        this.auditService = auditService;
    }

    @Transactional
    public void verifyContract(String contractId, String userId) {
        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new IllegalArgumentException("Contract not found"));
        
        String oldStatus = contract.getStatus();
        contract.verify();
        contractRepository.save(contract);
        
        auditService.logChange(contractId, "status", oldStatus, contract.getStatus(), "STATE_TRANSITION", userId);
    }

    @Transactional
    public void archiveContract(String contractId, String userId) {
        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new IllegalArgumentException("Contract not found"));
        
        contract.archive();
        contractRepository.save(contract);
        
        auditService.logChange(contractId, "status", null, "ARCHIVED", "ARCHIVE", userId);
    }
}
