package com.contract.master.service;

import com.contract.master.dto.ContractDTO;
import com.contract.master.entity.ContractBase;
import com.contract.master.repository.ContractBaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class ImportService {

    @Autowired
    private ContractBaseRepository contractBaseRepository;

    @Transactional
    public void importFromCsv(List<String[]> rows) {
        if (rows.isEmpty()) return;
        for (int i = 1; i < rows.size(); i++) {
            String[] row = rows.get(i);
            if (row.length < 5) continue;
            
            ContractBase contract = new ContractBase();
            contract.setContractId(UUID.randomUUID().toString());
            contract.setContractNo(row[0]);
            contract.setContractName(row[1]);
            contract.setPartyAName(row[2]);
            contract.setPartyBName(row[3]);
            try {
                contract.setContractAmount(new BigDecimal(row[4].trim()));
            } catch (Exception e) {
                contract.setContractAmount(BigDecimal.ZERO);
            }
            contract.setContractStatus("ACTIVE");
            
            contractBaseRepository.save(contract);
        }
    }
}
