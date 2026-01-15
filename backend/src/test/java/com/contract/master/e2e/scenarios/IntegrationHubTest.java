package com.contract.master.e2e.scenarios;

import com.contract.master.e2e.E2ETestBase;
import com.microsoft.playwright.Page;
import org.junit.jupiter.api.Test;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class IntegrationHubTest extends E2ETestBase {

    @Test
    void testFieldMappingManagement() {
        login("admin", "password", "tenant-1");
        page.navigate(baseUrl + "/integrations/mapping");
        assertThat(page.locator("h1")).containsText("Field Mapping");
        page.click("button:has-text('Add Mapping')");
        
        page.click(".el-select");
        page.waitForSelector(".el-select-dropdown:visible");
        page.click("li.el-select-dropdown__item:has-text('Contract Amount')");
        
        page.fill("input[placeholder='e.g. amount_total']", "contract_value");
        page.click(".el-dialog__footer button:has-text('Save')", new com.microsoft.playwright.Page.ClickOptions().setForce(true));
        assertThat(page.locator(".table-card table").first()).containsText("contract_value");
    }

    @Test
    void testWebHookManagement() {
        login("admin", "password", "tenant-1");
        page.navigate(baseUrl + "/integrations/webhooks");
        assertThat(page.locator("h1")).containsText("Outbound WebHooks");
        page.click("button:has-text('Create WebHook')");
        page.fill("input[placeholder*='ERP Finance Sync']", "E2E Test WebHook");
        page.fill("input[placeholder*='webhooks/contracts']", "https://example.com/webhook");
        page.click(".el-dialog__footer button:has-text('Save')", new com.microsoft.playwright.Page.ClickOptions().setForce(true));
        assertThat(page.locator("table").first()).containsText("E2E Test WebHook");
    }

    @Test
    void testSecretsManagement() {
        login("admin", "password", "tenant-1");
        page.navigate(baseUrl + "/integrations/secrets");
        assertThat(page.locator("h1")).containsText("Secrets & Keys");
        page.click("button:has-text('Generate New Key')");
        page.fill("input[placeholder='e.g. ERP-Production-Sync']", "E2E API Key");
        page.click(".el-dialog__footer button:has-text('Generate')", new Page.ClickOptions().setForce(true));
        assertThat(page.locator(".bg-gray-900")).containsText("AK_");
        page.click(".el-dialog__footer button:has-text('Done')", new Page.ClickOptions().setForce(true));
    }

    @Test
    void testConnectorConfiguration() {
        login("admin", "password", "tenant-1");
        page.navigate(baseUrl + "/integrations");
        
        page.click("button:has-text('Configure')");
        assertThat(page.locator(".el-dialog")).isVisible();
        page.fill("input[placeholder*='System Name']", "Updated Salesforce");
        page.click("button:has-text('Save')");
        assertThat(page.locator(".el-message--success")).isVisible();
    }

    @Test
    void testIntegrationHealthDashboard() {
        login("admin", "password", "tenant-1");
        page.navigate(baseUrl + "/integrations");
        assertThat(page.locator(".glass-card").first()).containsText("Integration Hub");
        assertThat(page.locator(".health-metrics")).isVisible();
    }

    @Test
    void testRetryFailedSync() {
        login("admin", "password", "tenant-1");
        page.navigate(baseUrl + "/integrations");
        page.locator("button.retry-btn").first().click(new com.microsoft.playwright.Locator.ClickOptions().setForce(true));
        assertThat(page.locator(".el-message--success")).isVisible();
    }
}
