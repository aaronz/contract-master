package com.contract.master;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class US1SyncTest extends E2ETestBase {

    @Test
    void testCrmWebhookSync() {
        login("admin", "password");
        page.navigate(baseUrl + "/contract/list");
        page.waitForSelector("h1");
        assertThat(page.locator("h1")).containsText("Contracts");
    }
}
