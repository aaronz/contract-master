package com.contract.master;

import com.contract.master.domain.ContractBase;
import com.contract.master.domain.ContractBaseRepository;
import com.contract.master.security.TenantContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.security.test.context.support.WithMockUser;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "admin", roles = {"ADMIN"})
public class BackendStabilizationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ContractBaseRepository contractBaseRepository;

    @Test
    void testTenantIsolationAtApiLevel() throws Exception {
        mockMvc.perform(get("/api/contracts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testContractVerificationLogic() throws Exception {
        ContractBase contract = new ContractBase();
        contract.setContractId("TEST-VERIFY-1");
        contract.setTenantId("tenant-1");
        contractBaseRepository.save(contract);

        mockMvc.perform(post("/api/contracts/TEST-VERIFY-1/verify")
                .header("X-Tenant-ID", "tenant-1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"verified\": true}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200));
    }

    @Test
    void testWebHookConfigCrud() throws Exception {
        mockMvc.perform(post("/api/webhook/configs")
                .header("X-Tenant-ID", "tenant-1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"ERP Sync\", \"url\": \"http://erp.local\", \"enabled\": true}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("ERP Sync"));
    }
}
