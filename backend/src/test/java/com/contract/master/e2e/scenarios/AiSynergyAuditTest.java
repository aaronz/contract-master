package com.contract.master.e2e.scenarios;

import com.contract.master.e2e.E2ETestBase;
import com.microsoft.playwright.Page;
import org.junit.jupiter.api.Test;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class AiSynergyAuditTest extends E2ETestBase {

    @Test
    void testAiToManualTransition() {
        login("admin", "password", "tenant-1");
        page.navigate(baseUrl + "/contracts/1");
        
        page.click("button:has-text('AI Analysis')");
        page.waitForSelector(".el-progress", new Page.WaitForSelectorOptions().setState(com.microsoft.playwright.options.WaitForSelectorState.HIDDEN));
        
        assertThat(page.locator(".el-tag--warning").first()).containsText("AI");
        
        page.click("span:has-text('Edit')");
        
        page.waitForSelector("input[name=\"custom_field_1\"]");
        page.fill("input[name=\"custom_field_1\"]", "New Manual Value");
        page.click("button.save-btn", new Page.ClickOptions().setForce(true));
        
        assertThat(page.locator(".el-tag--info").first()).isVisible();
    }
}
