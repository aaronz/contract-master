package com.contract.master.contract.application;

import com.contract.master.contract.domain.model.Contract;
import com.contract.master.contract.domain.model.ContractId;
import com.contract.master.contract.domain.model.ContractNo;
import com.contract.master.shared.domain.model.TenantId;
import com.contract.master.contract.domain.repository.ContractRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;

@Service
public class ContractApplicationServiceImpl implements ContractApplicationService {
    private final ContractRepository contractRepository;

    public ContractApplicationServiceImpl(ContractRepository contractRepository) {
        this.contractRepository = contractRepository;
    }

    @Override
    @Transactional
    public ContractId createContract(TenantId tenantId, ContractNo contractNo, String name) {
        ContractId id = ContractId.generate();
        Contract contract = new Contract(id, tenantId, contractNo);
        contract.setContractName(name);
        contractRepository.save(contract);
        return id;
    }

    @Override
    @Transactional
    public void updateContract(ContractId id, String name, LocalDate signDate) {
        Contract contract = contractRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Contract not found"));
        
        if (name != null) {
            contract.setContractName(name);
        }
        if (signDate != null) {
            contract.sign(signDate);
        }
        contractRepository.save(contract);
    }

    @Override
    public Contract getContract(ContractId id) {
        return contractRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Contract not found"));
    }

    @Override
    @Transactional
    public void verifyContract(String id, String userName) {
        Contract contract = contractRepository.findById(ContractId.of(id))
                .orElseThrow(() -> new IllegalArgumentException("Contract not found"));
        contract.verify();
        contractRepository.save(contract);
    }

    @Override
    @Transactional
    public void archiveContract(String id, String userName) {
        Contract contract = contractRepository.findById(ContractId.of(id))
                .orElseThrow(() -> new IllegalArgumentException("Contract not found"));
        contract.archive();
        contractRepository.save(contract);
    }
}
