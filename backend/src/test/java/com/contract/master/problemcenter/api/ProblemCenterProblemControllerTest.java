package com.contract.master.problemcenter.api;

import com.contract.master.problemcenter.application.ProblemService;
import com.contract.master.problemcenter.domain.model.Problem;
import com.contract.master.problemcenter.domain.model.ProblemStatus;
import com.contract.master.problemcenter.domain.repository.ProblemRepository;
import com.contract.master.shared.domain.model.TenantId;
import com.contract.master.security.TenantContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ProblemCenterProblemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProblemRepository problemRepository;

    @MockBean
    private ProblemService problemService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    public void testListProblems() throws Exception {
        Problem p = new Problem();
        p.setGeneratedMessage("Issue 1");
        
        when(problemRepository.findByContractId(eq("contract-1"))).thenReturn(Arrays.asList(p));

        mockMvc.perform(get("/api/problems")
                        .param("contractId", "contract-1")
                        .header("X-Tenant-ID", "tenant-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].generatedMessage").value("Issue 1"));
    }

    @Test
    @WithMockUser
    public void testUpdateProblemStatus() throws Exception {
        Problem p = new Problem();
        p.setStatus(ProblemStatus.ACKNOWLEDGED);
        
        when(problemService.updateStatus(eq(1L), eq(ProblemStatus.ACKNOWLEDGED), any())).thenReturn(p);
        when(problemRepository.save(any())).thenReturn(p);

        String json = "{\"status\": \"ACKNOWLEDGED\", \"notes\": \"working on it\"}";

        mockMvc.perform(put("/api/problems/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("ACKNOWLEDGED"));
    }
}
