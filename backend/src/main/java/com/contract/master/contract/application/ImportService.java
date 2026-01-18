package com.contract.master.contract.application;

import com.contract.master.contract.domain.model.ContractId;
import com.contract.master.contract.domain.model.ContractNo;
import com.contract.master.contract.domain.model.ContractAmount;
import com.contract.master.contract.domain.model.ContractParty;
import com.contract.master.contract.domain.model.Contract;
import com.contract.master.contract.domain.repository.ContractRepository;
import com.contract.master.shared.domain.model.TenantId;
import com.contract.master.security.TenantContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class ImportService {

    @Autowired
    private ContractRepository contractRepository;

    @Transactional
    public void importFromCsv(List<String[]> rows) {
        if (rows.isEmpty()) return;
        for (int i = 1; i < rows.size(); i++) {
            String[] row = rows.get(i);
            if (row.length < 5) continue;
            
            Contract contract = new Contract();
            contract.setContractId(ContractId.of(UUID.randomUUID().toString()));
            contract.setContractNo(new ContractNo(row[0]));
            contract.setTenantId(TenantId.of(TenantContext.getCurrentTenant()));
            contract.setContractName(row[1]);
            contract.setPartyA(new ContractParty(null, row[2], null, null, null));
            contract.setPartyB(new ContractParty(null, row[3], null, null, null));
            try {
                contract.setAmount(ContractAmount.of(new BigDecimal(row[4].trim()), "USD"));
            } catch (Exception e) {
                contract.setAmount(ContractAmount.of(BigDecimal.ZERO, "USD"));
            }
            contract.setStatus("ACTIVE");
            
            contractRepository.save(contract);
        }
    }
}
