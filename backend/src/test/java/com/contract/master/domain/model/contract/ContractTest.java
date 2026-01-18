package com.contract.master.domain.model.contract;
import com.contract.master.shared.domain.model.TenantId;

import com.contract.master.contract.domain.model.Contract;
import com.contract.master.contract.domain.model.ContractId;
import com.contract.master.contract.domain.model.ContractNo;
import com.contract.master.shared.domain.model.TenantId;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;

public class ContractTest {
    @Test
    void shouldCreateContract() {
        ContractId id = ContractId.generate();
        TenantId tenantId = TenantId.of("tenant-1");
        ContractNo contractNo = new ContractNo("CN-2026-001");
        Contract contract = new Contract(id, tenantId, contractNo);

        assertThat(contract.getContractId()).isEqualTo(id);
        assertThat(contract.getTenantId()).isEqualTo(tenantId);
        assertThat(contract.getContractNo()).isEqualTo(contractNo);
        assertThat(contract.getStatus()).isEqualTo("DRAFT");
    }

    @Test
    void shouldSignContract() {
        Contract contract = new Contract(ContractId.generate(), TenantId.of("tenant-1"), new ContractNo("CN-001"));
        LocalDate signDate = LocalDate.now();
        contract.sign(signDate);

        assertThat(contract.getStatus()).isEqualTo("SIGNED");
    }
}
