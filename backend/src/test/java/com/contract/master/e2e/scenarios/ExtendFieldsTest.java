package com.contract.master.e2e.scenarios;

import com.contract.master.e2e.E2ETestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

@DisplayName("US1: Extended Fields Management")
public class ExtendFieldsTest extends E2ETestBase {

    @Test
    @DisplayName("Create an extended field and verify its visibility in lists")
    void testExtendFieldVisibility() {
        login("admin", "password", "tenant-1");
        
        page.navigate(baseUrl + "/settings/fields");
        page.click("button:has-text('Add Field')");
        String fieldCode = "project_manager_" + System.currentTimeMillis();
        page.fill("input[placeholder*='project_manager']", fieldCode);
        page.fill("input[placeholder*='Project Manager']", "Project Manager");
        page.click("button:has-text('Add')");
        
        assertThat(page.locator("table").first()).containsText(fieldCode);
        
        page.navigate(baseUrl + "/contracts");
        page.waitForSelector(".el-table__header-wrapper");
        assertThat(page.locator(".el-table__header-wrapper")).containsText("Project Manager");
        
        page.click("button:has-text('New Contract')");
        assertThat(page.locator(".el-dialog")).containsText("Project Manager");
    }
}
