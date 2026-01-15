package com.contract.master.e2e.scenarios;

import com.contract.master.e2e.E2ETestBase;
import org.junit.jupiter.api.Test;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class AiSynergyAuditTest extends E2ETestBase {

    @Test
    void testAiToManualTransition() {
        page.navigate("http://localhost:5173/contract/detail/1");
        assertThat(page.locator(".el-tag--warning")).containsText("AI");
        page.fill("input[name=\"custom_field_1\"]", "New Manual Value");
        page.click("button.save-btn");
        assertThat(page.locator(".el-tag--info")).isVisible();
    }
}
