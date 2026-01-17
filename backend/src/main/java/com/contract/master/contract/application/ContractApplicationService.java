package com.contract.master.contract.application;

import com.contract.master.contract.domain.model.Contract;
import com.contract.master.contract.domain.repository.ContractRepository;
import com.contract.master.contract.domain.model.ContractBase;
import com.contract.master.contract.domain.repository.ContractBaseRepository;
import com.contract.master.dto.ContractDTO;
import com.contract.master.audit.application.AuditService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class ContractApplicationService {
    private final ContractRepository contractRepository;
    private final ContractBaseRepository contractBaseRepository;
    private final AuditService auditService;

    public ContractApplicationService(ContractRepository contractRepository, 
                                    ContractBaseRepository contractBaseRepository,
                                    AuditService auditService) {
        this.contractRepository = contractRepository;
        this.contractBaseRepository = contractBaseRepository;
        this.auditService = auditService;
    }

    @Transactional
    public ContractDTO createContract(ContractDTO dto) {
        Contract contract = new Contract();
        contract.setContractId(UUID.randomUUID().toString());
        contract.setContractNo(dto.getContractNo());
        contract.setContractName(dto.getContractName());
        contract.setStatus("DRAFT");
        
        Contract saved = contractRepository.save(contract);
        auditService.logChange(saved.getContractId(), "contract_base", null, "CREATED", "MANUAL", "admin");
        return dto; 
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
