package com.contract.master;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class US3ConfigTest extends E2ETestBase {

    @Test
    void testCustomFieldAndRuleFlow() {
        login("admin", "password");
        page.navigate(baseUrl + "/settings/fields");
        page.click("button:has-text('New Field')");
        page.navigate(baseUrl + "/settings/rules");
        assertThat(page.locator("h1")).containsText("Fields");
    }
}
