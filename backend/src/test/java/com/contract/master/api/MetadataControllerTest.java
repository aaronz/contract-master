package com.contract.master.api;

import com.contract.master.dto.FieldMetadataDTO;
import com.contract.master.service.MetadataService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class MetadataControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MetadataService metadataService;

    @Test
    @WithMockUser
    void testGetContractFields() throws Exception {
        FieldMetadataDTO dto = new FieldMetadataDTO("test", "Test", "TEXT", "STANDARD");
        when(metadataService.getContractFields()).thenReturn(Collections.singletonList(dto));

        mockMvc.perform(get("/api/metadata/contract-fields"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data[0].fieldCode").value("test"));
    }
}
