package com.contract.master.api;
import com.contract.master.shared.domain.model.TenantId;

import com.contract.master.rule.domain.model.RuleConfig;
import com.contract.master.evaluation.domain.repository.RuleConfigRepository;
import com.contract.master.shared.domain.model.TenantId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get; // Add this import
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.hasItem; // Import hasItem

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@EmbeddedKafka(partitions = 1, topics = {"contract-evaluation"})
public class RuleEngineControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RuleConfigRepository ruleRepository;

    @Test
    @WithMockUser
    public void testGetRuleDetail() throws Exception {
        RuleConfig rule = new RuleConfig();
        String businessId = "rule-" + java.util.UUID.randomUUID();
        rule.setRuleId(businessId);
        rule.setRuleName("Detail Rule");
        rule.setTenantId(TenantId.of("tenant-1"));
        rule.setIsEnabled(true);
        rule = ruleRepository.save(rule);

        mockMvc.perform(get("/api/rule-configs/" + businessId)
                        .header("X-Tenant-ID", "tenant-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.ruleName").value("Detail Rule"));
    }
}
