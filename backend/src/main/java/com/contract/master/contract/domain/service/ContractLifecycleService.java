package com.contract.master.contract.domain.service;

import com.contract.master.contract.domain.model.Contract;
import java.time.LocalDate;

public interface ContractLifecycleService {
    void signContract(Contract contract, LocalDate signDate);
    void activateContract(Contract contract, LocalDate effectiveDate);
}
