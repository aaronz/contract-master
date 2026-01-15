package com.contract.master.e2e.scenarios;

import com.contract.master.e2e.E2ETestBase;
import org.junit.jupiter.api.Test;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class UserJourneyAuditTest extends E2ETestBase {

    @Test
    void testFullContractLifecycleProbe() {
        login("admin", "password", "tenant-1");
        page.navigate(baseUrl + "/contract/list");
        page.click(".el-table__row:nth-child(1) button");
        boolean hasVerifyBtn = page.isVisible("button:has-text('Confirm Suggestions')");
        boolean hasPublishBtn = page.isVisible("button:has-text('Publish')");
        page.navigate(baseUrl + "/settings/downstream");
    }
}
