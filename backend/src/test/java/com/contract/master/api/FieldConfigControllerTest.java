package com.contract.master.api;
import com.contract.master.shared.domain.model.TenantId;

import com.contract.master.contract.metadata.domain.model.FieldConfig;
import com.contract.master.contract.metadata.domain.repository.FieldConfigRepository;
import com.contract.master.shared.domain.model.TenantId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class FieldConfigControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FieldConfigRepository fieldConfigRepository;

    @Test
    @WithMockUser
    public void testGetConfigsWithTenant() throws Exception {
        FieldConfig config = new FieldConfig();
        config.setFieldCode("test_field");
        config.setTenantId(TenantId.of("tenant_a"));
        config.setConfigType("DEFAULT"); // Add configType
        fieldConfigRepository.save(config);

        mockMvc.perform(get("/api/settings/fields")
                        .header("X-Tenant-ID", "tenant_a"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].fieldCode").value("test_field"));
    }

    @Test
    @WithMockUser
    public void testGetConfigsWithoutTenant() throws Exception {
        FieldConfig config = new FieldConfig();
        config.setFieldCode("global_field");
        config.setTenantId(TenantId.of("tenant_b"));
        config.setConfigType("DEFAULT"); // Add configType
        fieldConfigRepository.save(config);

        mockMvc.perform(get("/api/settings/fields"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @WithMockUser
    public void testBatchSaveConfigs() throws Exception {
        String payload = "[" +
                "{\"fieldCode\":\"field1\",\"fieldAlias\":\"Alias1\",\"isVisible\":true,\"displayOrder\":1, \"configType\":\"CONTRACT\"}," +
                "{\"fieldCode\":\"field2\",\"fieldAlias\":\"Alias2\",\"isVisible\":false,\"displayOrder\":2, \"configType\":\"CONTRACT\"}" +
                "]";

        mockMvc.perform(post("/api/settings/fields/batch")
                        .header("X-Tenant-ID", "tenant_c")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/settings/fields")
                        .header("X-Tenant-ID", "tenant_c"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()").value(2))
                .andExpect(jsonPath("$.data[0].fieldAlias").value("Alias1"));
    }

    @Test
    @WithMockUser
    public void testUnauthorizedTenantUpdate() throws Exception {
        FieldConfig config = new FieldConfig();
        config.setFieldCode("private_field");
        config.setTenantId(TenantId.of("tenant_x"));
        config.setConfigType("DEFAULT"); // Add configType
        FieldConfig saved = fieldConfigRepository.save(config);

        String payload = "[{\"id\":" + saved.getId() + ",\"fieldCode\":\"hacked\",\"fieldAlias\":\"Hacked\"}]";

        mockMvc.perform(post("/api/settings/fields/batch")
                        .header("X-Tenant-ID", "tenant_y")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isInternalServerError());
    }
}
