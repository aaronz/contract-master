package com.contract.master.problemcenter.api;

import com.contract.master.problemcenter.application.EvaluationService;
import com.contract.master.problemcenter.interfaces.rest.ProblemEvaluationController;
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
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ProblemEvaluationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EvaluationService evaluationService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    public void testTriggerEvaluation_Success() throws Exception {
        UUID contractId = UUID.randomUUID();
        java.util.List<String> ruleIds = Arrays.asList("rule-1", "rule-2");
        
        ProblemEvaluationController.EvaluationTriggerRequest request = new ProblemEvaluationController.EvaluationTriggerRequest();
        request.setContractId(contractId);
        request.setRuleIds(ruleIds);

        when(evaluationService.startEvaluation(eq(contractId), eq(ruleIds))).thenReturn(123L);

        mockMvc.perform(post("/api/problem-center/evaluations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.status").value(202))
                .andExpect(jsonPath("$.data").value(123));
    }

    @Test
    @WithMockUser
    public void testTriggerEvaluation_WithoutRules() throws Exception {
        UUID contractId = UUID.randomUUID();
        
        ProblemEvaluationController.EvaluationTriggerRequest request = new ProblemEvaluationController.EvaluationTriggerRequest();
        request.setContractId(contractId);

        when(evaluationService.startEvaluation(eq(contractId), any())).thenReturn(456L);

        mockMvc.perform(post("/api/problem-center/evaluations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.status").value(202))
                .andExpect(jsonPath("$.data").value(456));
    }
}
