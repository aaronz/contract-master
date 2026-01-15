package com.contract.master.e2e.scenarios;

import com.contract.master.e2e.E2ETestBase;
import org.junit.jupiter.api.Test;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class UserJourneyAuditTest extends E2ETestBase {

    @Test
    void testFullContractLifecycleProbe() {
        page.navigate("http://localhost:5173/contract/list");
        page.click("button.action-view:nth-child(1)");
        boolean hasVerifyBtn = page.isVisible("button:has-text('Confirm AI')");
        boolean hasPublishBtn = page.isVisible("button:has-text('Publish')");
        page.navigate("http://localhost:5173/settings/downstream");
    }
}
