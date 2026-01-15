package com.contract.master.e2e.scenarios;

import com.contract.master.e2e.E2ETestBase;
import org.junit.jupiter.api.Test;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class IntegrationHubTest extends E2ETestBase {

    @Test
    void testFieldMappingManagement() {
        page.navigate("http://localhost:5173/integrations/mapping");
        assertThat(page.locator("h1")).containsText("Field Mapping");
        page.click("button:has-text('Add Mapping')");
        page.selectOption("select", "amount");
        page.fill("input[placeholder='e.g. amount_total']", "contract_value");
        page.click("button:has-text('Save')");
        assertThat(page.locator("table")).containsText("contract_value");
    }

    @Test
    void testWebHookManagement() {
        page.navigate("http://localhost:5173/integrations/webhooks");
        assertThat(page.locator("h1")).containsText("Outbound WebHooks");
        page.click("button:has-text('Create WebHook')");
        page.fill("input[placeholder='e.g. ERP Finance Sync']", "E2E Test WebHook");
        page.fill("input[placeholder='https://api.yourcompany.com/webhooks/contracts']", "https://example.com/webhook");
        page.click("button:has-text('Save')");
        assertThat(page.locator("table")).containsText("E2E Test WebHook");
    }

    @Test
    void testSecretsManagement() {
        page.navigate("http://localhost:5173/integrations/secrets");
        assertThat(page.locator("h1")).containsText("Secrets & Keys");
        page.click("button:has-text('Generate New Key')");
        page.fill("input[placeholder='e.g. ERP-Production-Sync']", "E2E API Key");
        page.click("button:has-text('Generate')");
        assertThat(page.locator(".bg-gray-900")).containsText("AK_");
        page.click("button:has-text('Done')");
    }

    @Test
    void testIntegrationHealthDashboard() {
        page.navigate("http://localhost:5173/integrations");
        assertThat(page.locator(".glass-card")).containsText("Integration Hub");
        assertThat(page.locator(".health-metrics")).isVisible();
    }

    @Test
    void testRetryFailedSync() {
        page.navigate("http://localhost:5173/integrations");
        page.click("button.retry-btn:nth-child(1)");
        assertThat(page.locator(".el-message--success")).isVisible();
    }
}
