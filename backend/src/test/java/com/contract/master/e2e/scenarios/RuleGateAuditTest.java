package com.contract.master.e2e.scenarios;

import com.contract.master.e2e.E2ETestBase;
import org.junit.jupiter.api.Test;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class RuleGateAuditTest extends E2ETestBase {

    @Test
    void testRuleViolationBlocking() {
        page.navigate("http://localhost:5173/contract/new");
        page.fill("input[name=\"amount\"]", "2000000");
        page.click("button.sync-btn");
        assertThat(page.locator(".status-badge")).containsText("RISK_FLAGGED");
    }
}
