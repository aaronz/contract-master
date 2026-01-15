package com.contract.master.e2e.scenarios;

import com.contract.master.e2e.E2ETestBase;
import com.contract.master.e2e.pages.*;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("End-to-End Core Lifecycle Test")
public class FullLifecycleE2ETest extends E2ETestBase {

    @Test
    @DisplayName("Journey: Login -> Sync -> AI Extract -> Verify -> Publish -> Audit")
    void testCompleteContractJourney() {
        login("admin", "password", "tenant-1");
        page.waitForLoadState();
        
        page.navigate(baseUrl + "/integrations/webhooks");
        page.click("button:has-text('Create WebHook')", new Page.ClickOptions().setForce(true));
        page.fill("input[placeholder*='Integration Name']", "Auto-Test-ERP");
        page.fill("input[placeholder*='Payload URL']", "https://mock.erp.local/push");
        page.click("button:has-text('Save')", new Page.ClickOptions().setForce(true));

        page.navigate(baseUrl + "/contracts");
        ContractListPage listPage = new ContractListPage(page);
        listPage.search("CON-2026-STAB");
        ContractDetailPage detailPage = listPage.viewFirstDetail();

        detailPage.triggerAiAnalysis();
        assertThat(page.locator(".el-progress")).isVisible();
        page.waitForSelector(".el-progress", new Page.WaitForSelectorOptions().setState(WaitForSelectorState.HIDDEN));

        assertThat(page.locator(".el-tag--warning")).containsText("AI");
        
        page.click("button:has-text('Confirm Suggestions')");
        assertThat(page.locator(".el-message--success")).containsText("confirmed");

        page.click("button:has-text('Publish to Downstream')");
        assertThat(page.locator(".el-message--success")).containsText("Publication initiated");

        page.navigate(baseUrl + "/compliance/audit");
        assertThat(page.locator("table")).containsText("PUBLISHED");
        assertThat(page.locator("table")).containsText("New Value");

        APIResponse response = getAuthorized(baseUrl.replace("5173", "8080") + "/api/contracts/CON-2026-STAB");
        assertTrue(response.ok());
        assertTrue(response.text().contains("PUBLISHED"));
    }
}
