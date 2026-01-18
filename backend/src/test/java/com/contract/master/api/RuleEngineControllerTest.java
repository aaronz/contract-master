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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get; // Add this import
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.hasItem; // Import hasItem

@SpringBootTest
@AutoConfigureMockMvc
public class RuleEngineControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RuleConfigRepository ruleRepository;

    @Test
    @WithMockUser
    public void testListRules() throws Exception {
        RuleConfig rule = new RuleConfig();
        rule.setRuleId("test-rule-1");
        rule.setRuleName("Test Rule");
        rule.setTenantId(TenantId.of("tenant-1"));
        rule.setIsEnabled(true);
        ruleRepository.save(rule);

        mockMvc.perform(get("/api/rules")
                        .header("X-Tenant-ID", "tenant-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[*].ruleName", hasItem("Test Rule"))); // Assert presence instead of order
    }
}
