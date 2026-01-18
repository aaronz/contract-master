package com.contract.master.api;
import com.contract.master.shared.domain.model.TenantId;

import com.contract.master.contract.domain.model.ContractId;
import com.contract.master.contract.domain.model.ContractNo;
import com.contract.master.contract.domain.model.Contract;
import com.contract.master.contract.domain.model.ContractAmount;
import com.contract.master.contract.domain.model.ContractParty;
import com.contract.master.contract.domain.repository.ContractRepository;
import com.contract.master.contract.metadata.domain.model.FieldConfig;
import com.contract.master.contract.metadata.domain.repository.FieldConfigRepository;
import com.contract.master.contract.application.ContractService;
import com.contract.master.shared.domain.model.TenantId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ContractControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ContractRepository contractRepository;

    @Autowired
    private FieldConfigRepository fieldConfigRepository;

    @Autowired
    private ContractService contractService;

    @BeforeEach
    void setup() {
        contractService.clearFieldConfigCache();
    }

    @AfterEach
    void tearDown() {
        contractService.clearFieldConfigCache();
    }


    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testDataMasking() throws Exception {
        String contractIdStr = UUID.randomUUID().toString();
        Contract contract = new Contract();
        contract.setContractId(ContractId.of(contractIdStr));
        contract.setContractNo(new ContractNo("C123"));
        contract.setContractName("Acme Contract");
        contract.setPartyA(new ContractParty(null, "Acme", null, "13812345678", null));
        contract.setAmount(ContractAmount.of(new BigDecimal("1000.00"), "USD"));
        contract.setTenantId(TenantId.of("tenant_1"));
        contractRepository.save(contract);

        mockMvc.perform(get("/api/contracts/" + contractIdStr)
                        .header("X-Tenant-ID", "tenant_1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.partyAPhone").value("138****5678"))
                .andExpect(jsonPath("$.data.contractAmount").value(1000.00));
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testFieldFiltering() throws Exception {
        contractService.clearFieldConfigCache();

        String contractIdStr = UUID.randomUUID().toString();
        Contract contract = new Contract();
        contract.setContractId(ContractId.of(contractIdStr));
        contract.setContractNo(new ContractNo("C123"));
        contract.setContractName("Acme Contract");
        contract.setPartyA(new ContractParty(null, "Acme", null, "13812345678", null));
        contract.setAmount(ContractAmount.of(new BigDecimal("1000.00"), "USD"));
        contract.setTenantId(TenantId.of("tenant_1"));
        contractRepository.save(contract);

        FieldConfig contractAmountConfig = new FieldConfig();
        contractAmountConfig.setFieldCode("contract_amount");
        contractAmountConfig.setApiReturn(false);
        contractAmountConfig.setTenantId(TenantId.of("tenant_1"));
        contractAmountConfig.setConfigType("CONTRACT");
        fieldConfigRepository.save(contractAmountConfig);

        FieldConfig partyAPhoneConfig = new FieldConfig();
        partyAPhoneConfig.setFieldCode("party_a_phone");
        partyAPhoneConfig.setApiReturn(false);
        partyAPhoneConfig.setTenantId(TenantId.of("tenant_1"));
        partyAPhoneConfig.setConfigType("CONTRACT");
        fieldConfigRepository.save(partyAPhoneConfig);
        
        contractService.clearFieldConfigCache();

        mockMvc.perform(get("/api/contracts/" + contractIdStr)
                        .header("X-Tenant-ID", "tenant_1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.contractAmount").isEmpty())
                .andExpect(jsonPath("$.data.partyAPhone").isEmpty());
    }
}
