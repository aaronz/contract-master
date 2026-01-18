package com.contract.master;
import com.contract.master.shared.domain.model.TenantId;

import com.contract.master.contract.domain.model.ContractId;
import com.contract.master.contract.domain.model.Contract;
import com.contract.master.contract.domain.repository.ContractRepository;
import com.contract.master.security.TenantContext;
import com.contract.master.shared.domain.model.TenantId;
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
    private ContractRepository contractRepository;

    @Test
    void testTenantIsolationAtApiLevel() throws Exception {
        mockMvc.perform(get("/api/contracts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content").isEmpty());
    }


    @Test
    void testContractVerificationLogic() throws Exception {
        String contractIdStr = "550e8400-e29b-41d4-a716-446655440001";
        Contract contract = new Contract();
        contract.setContractId(ContractId.of(contractIdStr));
        contract.setTenantId(TenantId.of("tenant-1"));
        contractRepository.save(contract);

        mockMvc.perform(post("/api/contracts/" + contractIdStr + "/verify")
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
