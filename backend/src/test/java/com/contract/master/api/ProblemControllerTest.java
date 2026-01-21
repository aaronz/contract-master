package com.contract.master.api;
import com.contract.master.shared.domain.model.TenantId;

import com.contract.master.notification.application.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@SpringBootTest
@AutoConfigureMockMvc
public class ProblemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @MockBean
    private com.contract.master.problemcenter.application.ProblemService problemService;

    @MockBean
    private com.contract.master.problemcenter.domain.repository.ProblemRepository problemRepository;

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testResolveSuccess() throws Exception {
        mockMvc.perform(post("/api/problems/1/resolve")
                        .header("X-Tenant-ID", "tenant-1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200));

        verify(problemService).updateStatus(eq(1L), eq(com.contract.master.problemcenter.domain.model.ProblemStatus.RESOLVED), any());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testResolveForbidden() throws Exception {
        mockMvc.perform(post("/api/problems/1/resolve")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testResolveInvalidId() throws Exception {
        when(problemService.updateStatus(eq(0L), any(), any()))
                .thenThrow(new IllegalArgumentException("Problem not found: 0"));

        mockMvc.perform(post("/api/problems/0/resolve")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }
}
