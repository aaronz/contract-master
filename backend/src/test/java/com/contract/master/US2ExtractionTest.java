package com.contract.master;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class US2ExtractionTest extends E2ETestBase {

    @Test
    void testAiExtractionFlow() {
        page.navigate("http://localhost:5173/contract/detail/1");
        page.click("button:has-text('AI Analysis')");
        assertThat(page.locator(".contract-name")).isVisible();
    }
}
