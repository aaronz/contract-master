package com.contract.master.contract.application;

import com.contract.master.contract.domain.model.Contract;
import com.contract.master.contract.domain.model.ContractId;
import com.contract.master.contract.domain.model.ContractNo;
import com.contract.master.shared.domain.model.TenantId;
import java.time.LocalDate;

public interface ContractApplicationService {
    ContractId createContract(TenantId tenantId, ContractNo contractNo, String name);
    void updateContract(ContractId id, String name, LocalDate signDate);
    Contract getContract(ContractId id);
    void verifyContract(String id, String userName);
    void archiveContract(String id, String userName);
}
