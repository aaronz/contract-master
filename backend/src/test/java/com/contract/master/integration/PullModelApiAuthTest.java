package com.contract.master.integration;

import com.contract.master.integration.domain.model.DownstreamSystem;
import com.contract.master.integration.domain.repository.DownstreamSystemRepository;
import com.contract.master.shared.domain.model.TenantId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PullModelApiAuthTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DownstreamSystemRepository systemRepository;

    @Test
    void shouldReturn401WhenApiKeyIsMissing() throws Exception {
        mockMvc.perform(get("/api/v1/integration/contracts"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldReturn200WhenApiKeyIsValid() throws Exception {
        DownstreamSystem system = new DownstreamSystem();
        system.setSystemId("test-system");
        system.setAccessKey("valid-key");
        system.setIsEnabled(true);
        system.setTenantId(TenantId.of("tenant-1"));

        when(systemRepository.findByAccessKeyAndIsEnabledTrue("valid-key"))
                .thenReturn(Optional.of(system));

        mockMvc.perform(get("/api/v1/integration/contracts")
                .header("X-API-KEY", "valid-key"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturn401WhenApiKeyIsInvalid() throws Exception {
        when(systemRepository.findByAccessKeyAndIsEnabledTrue("invalid-key"))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/integration/contracts")
                .header("X-API-KEY", "invalid-key"))
                .andExpect(status().isUnauthorized());
    }
}
