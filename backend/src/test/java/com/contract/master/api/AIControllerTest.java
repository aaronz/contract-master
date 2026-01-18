package com.contract.master.api;
import com.contract.master.shared.domain.model.TenantId;

import com.contract.master.ai.application.AIService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AIControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AIService aiService;

    @Test
    @WithMockUser
    public void testUploadAndExtractSuccess() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "contract.pdf", "application/pdf", "dummy content".getBytes());
        Map<String, Object> result = new HashMap<>();
        result.put("contractNo", "EXT-001");
        when(aiService.extractContractData(anyString(), any())).thenReturn(result);

        mockMvc.perform(multipart("/api/contracts/ai-upload").file(file))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.contractNo").value("EXT-001"));
    }
}
