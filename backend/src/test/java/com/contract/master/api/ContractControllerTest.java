package com.contract.master.api;

import com.contract.master.domain.ContractBase;
import com.contract.master.domain.ContractBaseRepository;
import com.contract.master.domain.FieldConfig;
import com.contract.master.domain.FieldConfigRepository;
import com.contract.master.service.ContractService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional; // Import Transactional

import java.math.BigDecimal;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional // Add Transactional annotation
public class ContractControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ContractBaseRepository contractRepository;

    @Autowired
    private FieldConfigRepository fieldConfigRepository;

    @Autowired
    private com.contract.master.service.ContractService contractService; // Inject ContractService to clear cache

    @BeforeEach // Add BeforeEach
    void setup() {
        contractService.clearFieldConfigCache(); // Clear cache before each test
    }

    @AfterEach
    void tearDown() {
        contractService.clearFieldConfigCache(); // Clear cache after each test
    }


    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testDataMasking() throws Exception {
        String contractId = UUID.randomUUID().toString();
        ContractBase contract = new ContractBase();
        contract.setContractId(contractId);
        contract.setContractNo("C123");
        contract.setPartyAName("Acme");
        contract.setPartyAPhone("13812345678");
        contract.setAmount(new BigDecimal("1000.00")); // This field will not be filtered in this test
        contract.setTenantId("tenant_1");
        contractRepository.save(contract);

        // Assert that partyAPhone is masked when no FieldConfig hides it
        mockMvc.perform(get("/api/contracts/" + contractId)
                        .header("X-Tenant-ID", "tenant_1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.partyAPhone").value("138****5678"))
                .andExpect(jsonPath("$.data.contractAmount").value(1000.00)); // Should still be present
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testFieldFiltering() throws Exception {
        // Clear cache to ensure clean state for field configs
        contractService.clearFieldConfigCache();

        String contractId = UUID.randomUUID().toString();
        ContractBase contract = new ContractBase();
        contract.setContractId(contractId);
        contract.setContractNo("C123");
        contract.setPartyAName("Acme");
        contract.setPartyAPhone("13812345678");
        contract.setAmount(new BigDecimal("1000.00"));
        contract.setTenantId("tenant_1");
        contractRepository.save(contract);

        // Setup FieldConfig for contract_amount to be hidden (apiReturn = false)
        FieldConfig contractAmountConfig = new FieldConfig();
        contractAmountConfig.setFieldCode("contract_amount"); // Use snake_case for FieldCode
        contractAmountConfig.setApiReturn(false);
        contractAmountConfig.setTenantId("tenant_1");
        contractAmountConfig.setConfigType("CONTRACT");
        fieldConfigRepository.save(contractAmountConfig);

        // Setup FieldConfig for party_a_phone to be hidden (apiReturn = false)
        FieldConfig partyAPhoneConfig = new FieldConfig();
        partyAPhoneConfig.setFieldCode("party_a_phone"); // Use snake_case for FieldCode
        partyAPhoneConfig.setApiReturn(false);
        partyAPhoneConfig.setTenantId("tenant_1");
        partyAPhoneConfig.setConfigType("CONTRACT");
        fieldConfigRepository.save(partyAPhoneConfig);
        
        // Clear cache again to ensure new FieldConfigs are loaded by ContractService
        contractService.clearFieldConfigCache();

        // Assert that contractAmount and partyAPhone are hidden
        mockMvc.perform(get("/api/contracts/" + contractId)
                        .header("X-Tenant-ID", "tenant_1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.contractAmount").isEmpty())
                .andExpect(jsonPath("$.data.partyAPhone").isEmpty());
    }
}