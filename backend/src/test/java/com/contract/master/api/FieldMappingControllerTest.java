package com.contract.master.api;

import com.contract.master.domain.FieldMapping;
import com.contract.master.domain.FieldMappingRepository;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class FieldMappingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FieldMappingRepository repository;

    @Test
    @WithMockUser
    public void testListSuccess() throws Exception {
        when(repository.findByTenantId(any())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/settings/mapping"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    @WithMockUser
    public void testSaveSuccess() throws Exception {
        FieldMapping mapping = new FieldMapping();
        mapping.setInternalField("field1");
        when(repository.save(any())).thenReturn(mapping);

        mockMvc.perform(post("/api/v1/settings/mapping")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"internalField\":\"field1\"}"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    public void testDeleteSuccess() throws Exception {
        mockMvc.perform(delete("/api/v1/settings/mapping/1"))
                .andExpect(status().isOk());

        verify(repository).deleteById(1L);
    }
}
