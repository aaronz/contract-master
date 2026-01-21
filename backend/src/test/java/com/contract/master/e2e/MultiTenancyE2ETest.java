package com.contract.master.e2e;

import com.contract.master.contract.domain.model.Contract;
import com.contract.master.contract.domain.model.ContractId;
import com.contract.master.contract.domain.model.ContractNo;
import com.contract.master.contract.domain.repository.ContractRepository;
import com.contract.master.security.TenantContext;
import com.contract.master.shared.domain.model.TenantId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class MultiTenancyE2ETest {

    @Autowired
    private ContractRepository contractRepository;

    @AfterEach
    void tearDown() {
        TenantContext.clear();
    }

    @Test
    void shouldIsolateDataBetweenTenants() {
        String tenantA = "tenant-a";
        String tenantB = "tenant-b";
        
        TenantContext.setCurrentTenant(tenantA);
        Contract contractA = new Contract();
        contractA.setContractId(ContractId.of(UUID.randomUUID().toString()));
        contractA.setContractNo(new ContractNo("CON-A-001"));
        contractA.setContractName("Tenant A Contract");
        contractRepository.save(contractA);
        
        List<Contract> contractsA = contractRepository.findAll();
        
        assertThat(contractsA).extracting(Contract::getContractName).contains("Tenant A Contract");
        
        TenantContext.setCurrentTenant(tenantB);
        List<Contract> contractsB = contractRepository.findAll();
        
        assertThat(contractsB).isEmpty();
    }

    @Test
    void shouldBypassIsolationWithSystemContext() {
        String tenantA = "tenant-a";
        TenantContext.setCurrentTenant(tenantA);
        Contract contractA = new Contract();
        contractA.setContractId(ContractId.of(UUID.randomUUID().toString()));
        contractA.setContractNo(new ContractNo("CON-SYS-001"));
        contractA.setContractName("System Test Contract");
        contractRepository.save(contractA);
        
        TenantContext.executeAsSystem(() -> {
            List<Contract> allContracts = contractRepository.findAll();
            assertThat(allContracts).extracting(Contract::getContractName).contains("System Test Contract");
        });
    }
}
