package com.contract.master;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class US2ExtractionTest extends E2ETestBase {

    @Test
    void testAiExtractionFlow() {
        login("admin", "password");
        page.navigate(baseUrl + "/contract/detail/1");
        page.waitForSelector("button:has-text('AI Analysis')");
        page.click("button:has-text('AI Analysis')", new Page.ClickOptions().setForce(true));
        assertThat(page.locator(".contract-name")).isVisible();
    }
}
