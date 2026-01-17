package com.contract.master.api;

import com.contract.master.contract.domain.model.ContractExtendField;
import com.contract.master.contract.domain.repository.ContractExtendFieldRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ContractExtendFieldControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ContractExtendFieldRepository repository;

    @Test
    @WithMockUser
    public void testCreateExtendField() throws Exception {
        String payload = "{\"fieldCode\":\"new_field\",\"fieldName\":\"New Field\",\"fieldType\":\"TEXT\"}";

        mockMvc.perform(post("/api/settings/extend-fields")
                        .header("X-Tenant-ID", "tenant_1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.fieldCode").value("new_field"));
    }

    @Test
    @WithMockUser
    public void testCreateInvalidExtendField() throws Exception {
        String payload = "{\"fieldCode\":\"invalid field\",\"fieldName\":\"Invalid\",\"fieldType\":\"TEXT\"}";

        mockMvc.perform(post("/api/settings/extend-fields")
                        .header("X-Tenant-ID", "tenant_1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    public void testDeleteUnauthorizedField() throws Exception {
        ContractExtendField field = new ContractExtendField();
        field.setFieldId("f-100");
        field.setFieldCode("private");
        field.setFieldName("Private");
        field.setFieldType("TEXT");
        field.setTenantId("tenant_a");
        repository.save(field);

        mockMvc.perform(delete("/api/settings/extend-fields/f-100")
                        .header("X-Tenant-ID", "tenant_b"))
                .andExpect(status().isInternalServerError());
    }
}
