package com.contract.master.problemcenter;

import com.contract.master.contract.domain.model.Contract;
import com.contract.master.contract.domain.model.ContractId;
import com.contract.master.contract.domain.model.ContractNo;
import com.contract.master.contract.domain.repository.ContractRepository;
import com.contract.master.problemcenter.application.EvaluationService;
import com.contract.master.problemcenter.domain.model.Problem;
import com.contract.master.problemcenter.domain.model.ProblemStatus;
import com.contract.master.problemcenter.domain.repository.ProblemRepository;
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

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@SpringBootTest
@EmbeddedKafka(partitions = 1, topics = {"contract-evaluation"})
@ActiveProfiles("test")
@Disabled("Temporarily disabled")
public class ProblemRuleEngineIntegrationTest {

    @Autowired
    private EvaluationService evaluationService;

    @Autowired
    private RuleRepository ruleRepository;

    @Autowired
    private ProblemRepository problemRepository;

    @Autowired
    private ContractRepository contractRepository;

    @Test
    public void testEndToEndEvaluation() {
        TenantContext.setCurrentTenant("tenant-1");
        TenantId tenantId = TenantId.of("tenant-1");

        ContractId contractId = ContractId.generate();
        Contract contract = new Contract(contractId, tenantId, new ContractNo("CON-123"));
        contract.setContractName("Test Project High Risk");
        contractRepository.save(contract);

        Rule rule = new Rule();
        rule.setName("High Risk Rule");
        rule.setLogicType(RuleLogicType.GROOVY);
        rule.setLogicContent("contract.contractName.contains('High Risk')");
        rule.setSeverity(Severity.SEVERE);
        rule.setStatus(RuleStatus.ACTIVE);
        rule.setTenantId(tenantId);
        ruleRepository.save(rule);

        evaluationService.startEvaluation(contractId.value(), null);

        await().atMost(10, TimeUnit.SECONDS).untilAsserted(() -> {
            List<Problem> problems = problemRepository.findByContractId(contractId.value().toString());
            assertThat(problems).isNotEmpty();
            assertThat(problems.get(0).getGeneratedMessage()).contains("Rule hit: High Risk Rule");
            assertThat(problems.get(0).getStatus()).isEqualTo(ProblemStatus.NEW);
        });
    }
}
