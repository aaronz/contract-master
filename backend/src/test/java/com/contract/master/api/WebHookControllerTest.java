package com.contract.master.api;

import com.contract.master.integration.domain.model.WebHookConfig;
import com.contract.master.integration.domain.repository.WebHookConfigRepository;
import com.contract.master.integration.application.CrmIntegrationApplicationService;
import com.contract.master.shared.application.RateLimiterService;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.contract.master.integration.application.CrmIntegrationApplicationService;

@SpringBootTest
@AutoConfigureMockMvc
public class WebHookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CrmIntegrationApplicationService crmIntegrationService;

    @MockBean
    private RateLimiterService rateLimiterService;

    @MockBean
    private WebHookConfigRepository configRepository;

    @Test
    @WithMockUser
    public void testReceiveWebhookSuccess() throws Exception {
        when(rateLimiterService.tryConsume("CRM")).thenReturn(true);

        mockMvc.perform(post("/api/webhook/crm")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\":\"123\"}"))
                .andExpect(status().isOk());

        verify(crmIntegrationService).syncContract(any(), eq("CRM"));
    }

    @Test
    @WithMockUser
    public void testReceiveWebhookRateLimitExceeded() throws Exception {
        when(rateLimiterService.tryConsume("CRM")).thenReturn(false);

        mockMvc.perform(post("/api/webhook/crm")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\":\"123\"}"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testListConfigsSuccess() throws Exception {
        when(configRepository.findByTenantId(any())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/webhook/configs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testListConfigsForbidden() throws Exception {
        mockMvc.perform(get("/api/webhook/configs"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testSaveConfigSuccess() throws Exception {
        WebHookConfig config = new WebHookConfig();
        config.setName("Test Hook");
        when(configRepository.save(any())).thenReturn(config);

        mockMvc.perform(post("/api/webhook/configs")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Test Hook\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("Test Hook"));
    }
}
