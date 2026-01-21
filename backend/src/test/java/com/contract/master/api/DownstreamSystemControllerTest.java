package com.contract.master.api;
import com.contract.master.shared.domain.model.TenantId;

import com.contract.master.integration.domain.model.DownstreamSystem;
import com.contract.master.integration.domain.repository.DownstreamSystemRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class DownstreamSystemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DownstreamSystemRepository repository;

    @Test
    @WithMockUser
    public void testListSuccess() throws Exception {
        when(repository.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/settings/downstream"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    @WithMockUser
    public void testCreateSuccess() throws Exception {
        DownstreamSystem system = new DownstreamSystem();
        system.setSystemName("Test System");
        when(repository.save(any())).thenReturn(system);

        mockMvc.perform(post("/api/v1/settings/downstream")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"systemName\":\"Test System\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.systemName").value("Test System"));
    }

    @Test
    @WithMockUser
    public void testUpdateSuccess() throws Exception {
        DownstreamSystem system = new DownstreamSystem();
        system.setSystemId("sys-1");
        system.setSystemName("Updated System");
        when(repository.save(any())).thenReturn(system);

        mockMvc.perform(put("/api/v1/settings/downstream/sys-1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"systemName\":\"Updated System\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.systemName").value("Updated System"));
    }
}
