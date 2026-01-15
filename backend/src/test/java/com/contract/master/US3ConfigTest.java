package com.contract.master;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class US3ConfigTest extends E2ETestBase {

    @Test
    void testCustomFieldAndRuleFlow() {
        page.navigate("http://localhost:5173/settings/fields");
        page.click("button:has-text('New Field')");
        page.navigate("http://localhost:5173/settings/rules");
        assertThat(page.locator("h1")).containsText("Fields");
    }
}
