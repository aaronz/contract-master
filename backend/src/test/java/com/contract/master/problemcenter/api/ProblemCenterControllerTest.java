package com.contract.master.problemcenter.api;

import com.contract.master.evaluation.domain.model.EvaluationJob;
import com.contract.master.evaluation.domain.repository.EvaluationJobRepository;
import com.contract.master.shared.domain.model.TenantId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@EmbeddedKafka(partitions = 1, topics = {"contract-evaluation"})
public class ProblemCenterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EvaluationJobRepository evaluationJobRepository;

    @Test
    @WithMockUser
    public void testGetEvaluationJobs() throws Exception {
        String tenantId = "tenant-1";
        EvaluationJob job = new EvaluationJob(tenantId, EvaluationJob.TriggerType.MANUAL, "admin");
        job.setJobId("job-" + java.util.UUID.randomUUID());
        evaluationJobRepository.save(job);

        mockMvc.perform(get("/api/problem-center-legacy/evaluation-jobs")
                        .header("X-Tenant-ID", tenantId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }
}
