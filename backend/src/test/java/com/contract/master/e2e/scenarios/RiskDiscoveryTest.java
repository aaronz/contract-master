package com.contract.master.e2e.scenarios;

import com.contract.master.e2e.E2ETestBase;
import com.contract.master.e2e.pages.DashboardPO;
import com.contract.master.e2e.pages.LoginPO;
import com.contract.master.e2e.pages.ProblemCenterPO;
import com.contract.master.e2e.utils.TestDataGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@Disabled("Temporarily disabled")
public class RiskDiscoveryTest extends E2ETestBase {
    private DashboardPO dashboard;
    private TestDataGenerator generator;

    @BeforeEach
    void setup() {
        generator = new TestDataGenerator(playwright);
        generator.seedUser("admin", "password", "tenant-1");
        
        LoginPO loginPage = new LoginPO(page, getBaseUrl());
        loginPage.navigate();
        dashboard = loginPage.login("admin", "password", "tenant-1");
    }

    @Test
    void testRuleViolationDetection() {
        String contractNo = "RSK-" + System.nanoTime();
        Map<String, Object> contract = new HashMap<>();
        contract.put("contractNo", contractNo);
        contract.put("contractAmount", 2000000); // Trigger high value rule
        generator.seedContract("tenant-1", contract);

        ProblemCenterPO riskPage = dashboard.navigateToRisk();
        riskPage.selectContract(contractNo);
        riskPage.runRules();
        
        assertThat(riskPage.getProblemCount()).isGreaterThan(0);
    }
}
