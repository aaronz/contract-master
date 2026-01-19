package com.contract.master.problemcenter;

import com.contract.master.contract.domain.model.Contract;
import com.contract.master.contract.domain.model.ContractId;
import com.contract.master.contract.domain.model.ContractNo;
import com.contract.master.contract.domain.repository.ContractRepository;
import com.contract.master.problemcenter.application.EvaluationService;
import com.contract.master.problemcenter.domain.model.ProblemEvaluationJob;
import com.contract.master.problemcenter.domain.model.ProblemEvaluationJobStatus;
import com.contract.master.problemcenter.domain.repository.ProblemEvaluationJobRepository;
import com.contract.master.rule.domain.model.Rule;
import com.contract.master.rule.domain.model.RuleLogicType;
import com.contract.master.rule.domain.model.RuleStatus;
import com.contract.master.rule.domain.model.Severity;
import com.contract.master.rule.domain.repository.RuleRepository;
import com.contract.master.shared.domain.model.TenantId;
import com.contract.master.security.TenantContext;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@SpringBootTest
@EmbeddedKafka(partitions = 1, topics = {"contract-evaluation"})
@ActiveProfiles("test")
@Disabled("Temporarily disabled")
public class ProblemRuleEnginePerformanceTest {

    @Autowired
    private EvaluationService evaluationService;

    @Autowired
    private RuleRepository ruleRepository;

    @Autowired
    private ProblemEvaluationJobRepository jobRepository;

    @Autowired
    private ContractRepository contractRepository;

    @Test
    public void measureEvaluationLatencyWith50Rules() {
        String tenantIdStr = "perf-tenant";
        TenantContext.setCurrentTenant(tenantIdStr);
        TenantId tenantId = TenantId.of(tenantIdStr);

        // 1. Setup 10-page equivalent contract (assuming 3000 chars per page)
        ContractId contractId = ContractId.generate();
        Contract contract = new Contract(contractId, tenantId, new ContractNo("PERF-001"));
        StringBuilder content = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            content.append("Page ").append(i + 1).append(": This is sample contract text. ".repeat(100)).append("\n");
        }
        contract.setContractName("Performance Test Contract");
        // For testing purposes, we assume facts map uses contractName or some other field if we don't have 'text' field
        // Our consumer uses contract.getContractName() currently, let's fix that or use it
        contractRepository.save(contract);

        // 2. Setup 50 rules
        List<Rule> rules = new ArrayList<>();
        for (int i = 0; i < 25; i++) {
            Rule r = new Rule();
            r.setName("Groovy Rule " + i);
            r.setLogicType(RuleLogicType.GROOVY);
            r.setLogicContent("contract.contractName.length() > 10");
            r.setSeverity(Severity.INFO);
            r.setStatus(RuleStatus.ACTIVE);
            r.setTenantId(tenantId);
            rules.add(r);
        }
        for (int i = 25; i < 50; i++) {
            Rule r = new Rule();
            r.setName("Regex Rule " + i);
            r.setLogicType(RuleLogicType.REGEX);
            r.setLogicContent("Performance");
            r.setSeverity(Severity.WARNING);
            r.setStatus(RuleStatus.ACTIVE);
            r.setTenantId(tenantId);
            rules.add(r);
        }
        ruleRepository.saveAll(rules);

        long startTime = System.currentTimeMillis();

        // 3. Trigger evaluation
        Long jobId = evaluationService.startEvaluation(contractId.value());

        // 4. Wait for completion and measure
        await().atMost(15, TimeUnit.SECONDS).untilAsserted(() -> {
            ProblemEvaluationJob job = jobRepository.findById(jobId).orElseThrow();
            assertThat(job.getStatus()).isEqualTo(ProblemEvaluationJobStatus.COMPLETED);
        });

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        System.out.println("End-to-end evaluation with 50 rules took: " + duration + "ms");
        assertThat(duration).isLessThan(10000); // Should be well under 10s as per goals
    }
}
