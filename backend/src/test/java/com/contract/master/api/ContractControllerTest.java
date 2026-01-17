package com.contract.master.api;

import com.contract.master.domain.ContractBase;
import com.contract.master.domain.ContractBaseRepository;
import com.contract.master.domain.FieldConfig;
import com.contract.master.domain.FieldConfigRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ContractControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ContractBaseRepository contractRepository;

    @Autowired
    private FieldConfigRepository fieldConfigRepository;

    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testDataMaskingAndFieldFiltering() throws Exception {
        String contractId = UUID.randomUUID().toString();
        ContractBase contract = new ContractBase();
        contract.setContractId(contractId);
        contract.setContractNo("C123");
        contract.setPartyAName("Acme");
        contract.setPartyAPhone("13812345678");
        contract.setAmount(new BigDecimal("1000.00"));
        contract.setTenantId("tenant_1");
        contractRepository.save(contract);

        // 1. Check data masking for phone
        mockMvc.perform(get("/api/contracts/" + contractId)
                        .header("X-Tenant-ID", "tenant_1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.partyAPhone").value("138****5678"));

        // 2. Hide amount field via config
        FieldConfig config = new FieldConfig();
        config.setFieldCode("contract_amount");
        config.setApiReturn(false);
        config.setTenantId("tenant_1");
        config.setConfigType("CONTRACT");
        fieldConfigRepository.save(config);

        mockMvc.perform(get("/api/contracts/" + contractId)
                        .header("X-Tenant-ID", "tenant_1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.contractAmount").isEmpty());
    }
}
