package com.contract.master.e2e.scenarios;

import com.contract.master.e2e.E2ETestBase;
import org.junit.jupiter.api.Test;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class UserJourneyAuditTest extends E2ETestBase {

    @Test
    void testFullContractLifecycleProbe() {
        login("admin", "password", "tenant-1");
        page.navigate(baseUrl + "/contracts");
        page.locator(".el-table__row").first().locator("button").first().click();
        boolean hasVerifyBtn = page.locator("button:has-text('Confirm Suggestions')").first().isVisible();
        boolean hasPublishBtn = page.locator("button:has-text('Publish')").first().isVisible();
        page.navigate(baseUrl + "/settings/downstream");
    }
}
