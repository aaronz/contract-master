package com.contract.master.evaluation.api;

import com.contract.master.api.GlobalExceptionHandler;
import com.contract.master.dto.EvaluationTriggerRequest;
import com.contract.master.evaluation.application.EvaluationApplicationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.Map;
import java.util.List; // Added import for List
import java.util.Collections; // Added import for Collections

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class EvaluationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EvaluationApplicationService evaluationService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void testTriggerEvaluation_Success() throws Exception {
        String contractId = "contract123";
        List<String> ruleIds = Arrays.asList("rule1", "rule2");
        String jobId = "job456";

        EvaluationTriggerRequest request = new EvaluationTriggerRequest(Collections.singletonList(contractId), ruleIds);

        when(evaluationService.triggerReEvaluationForSingleContract(eq(contractId), eq(ruleIds), eq("testuser")))
                .thenReturn(jobId);

        mockMvc.perform(post("/api/evaluations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.status").value(202))
                .andExpect(jsonPath("$.data.jobId").value(jobId));
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void testTriggerEvaluation_InvalidContractIdCount() throws Exception {
        List<String> contractIds = Arrays.asList("contract1", "contract2"); // More than one
        List<String> ruleIds = Arrays.asList("rule1");

        EvaluationTriggerRequest request = new EvaluationTriggerRequest(contractIds, ruleIds);

        mockMvc.perform(post("/api/evaluations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Exactly one contract ID is required for re-evaluation."));
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void testTriggerEvaluation_ContractNotFound() throws Exception {
        String contractId = "nonExistentContract";
        List<String> ruleIds = Arrays.asList("rule1");

        EvaluationTriggerRequest request = new EvaluationTriggerRequest(Collections.singletonList(contractId), ruleIds);

        when(evaluationService.triggerReEvaluationForSingleContract(eq(contractId), eq(ruleIds), eq("testuser")))
                .thenThrow(new IllegalArgumentException("Contract not found"));

        mockMvc.perform(post("/api/evaluations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict()) // Illegal argument usually maps to 409 Conflict
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.message").value("Contract not found"));
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void testTriggerEvaluation_AlreadyInProgress() throws Exception {
        String contractId = "contractInProg";
        List<String> ruleIds = Arrays.asList("rule1");

        EvaluationTriggerRequest request = new EvaluationTriggerRequest(Collections.singletonList(contractId), ruleIds);

        when(evaluationService.triggerReEvaluationForSingleContract(eq(contractId), eq(ruleIds), eq("testuser")))
                .thenThrow(new IllegalStateException("An evaluation for this contract is already in progress."));

        mockMvc.perform(post("/api/evaluations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.message").value("An evaluation for this contract is already in progress."));
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void testTriggerEvaluation_InternalServerError() throws Exception {
        String contractId = "contract123";
        List<String> ruleIds = Arrays.asList("rule1");

        EvaluationTriggerRequest request = new EvaluationTriggerRequest(Collections.singletonList(contractId), ruleIds);

        when(evaluationService.triggerReEvaluationForSingleContract(any(), any(), any()))
                .thenThrow(new RuntimeException("Something unexpected happened"));

        mockMvc.perform(post("/api/evaluations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.message").exists());
    }
}
