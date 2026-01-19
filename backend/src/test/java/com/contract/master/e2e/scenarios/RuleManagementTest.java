package com.contract.master.e2e.scenarios;

import com.contract.master.e2e.E2ETestBase;
import com.contract.master.e2e.pages.DashboardPO;
import com.contract.master.e2e.pages.LoginPO;
import com.contract.master.e2e.pages.RuleManagementPO;
import com.contract.master.e2e.utils.TestDataGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@Disabled("Temporarily disabled")
public class RuleManagementTest extends E2ETestBase {
    private DashboardPO dashboard;

    @BeforeEach
    void setup() {
        TestDataGenerator generator = new TestDataGenerator(playwright);
        generator.seedUser("admin", "password", "tenant-1");

        LoginPO loginPage = new LoginPO(page, getBaseUrl());
        loginPage.navigate();
        dashboard = loginPage.login("admin", "password", "tenant-1");
    }


    @Test
    void testRuleCreation() {
        RuleManagementPO rulePage = dashboard.navigateToRules();
        
        Map<String, String> data = new HashMap<>();
        data.put("Rule Name", "High Value Check");
        // We'll focus on the essential rule name first. 
        // Other fields have default values in the UI (LOGIC, info, create).
        rulePage.createRule(data);
        
        assertThat(rulePage.getRuleList()).contains("High Value Check");
    }
}
