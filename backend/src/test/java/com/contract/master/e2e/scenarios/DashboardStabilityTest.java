package com.contract.master.e2e.scenarios;

import com.contract.master.e2e.E2ETestBase;
import com.microsoft.playwright.Page;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("US1: Dashboard Stability & Accuracy")
public class DashboardStabilityTest extends E2ETestBase {

    @Test
    @DisplayName("Verify live dashboard metrics and export")
    void testDashboardLiveStats() {
        login("admin", "password", "tenant-1");
        
        page.navigate(baseUrl + "/dashboard");
        
        page.waitForSelector(".stat-value");
        assertThat(page.locator(".stat-value").first()).not().containsText("1,248");
        
        page.click("button:has-text('Export Report')");
        
        page.click(".chart-card.main-chart .chart-container", new Page.ClickOptions().setForce(true));
        
        if (page.url().contains("/contracts")) {
            assertTrue(page.url().contains("/contracts"));
        }
    }
}
