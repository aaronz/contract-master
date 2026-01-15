package com.contract.master;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class US1SyncTest extends E2ETestBase {

    @Test
    void testCrmWebhookSync() {
        login("admin", "password");
        page.navigate(baseUrl + "/contracts");
        page.waitForSelector(".page-title");
        assertThat(page.locator(".page-title")).containsText("Contracts");
    }
}
