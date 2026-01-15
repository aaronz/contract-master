package com.contract.master;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class US3ConfigTest extends E2ETestBase {

    @Test
    void testCustomFieldAndRuleFlow() {
        login("admin", "password");
        page.navigate(baseUrl + "/settings/fields");
        page.waitForSelector(".page-title");
        assertThat(page.locator(".page-title")).containsText("Field Configuration");
        
        page.click("button:has-text('Add Field')");
        page.fill("input[placeholder*='project_manager']", "project_manager");
        page.fill("input[placeholder*='Project Manager']", "Project Manager");
        page.click("button:has-text('Add')");
        
        assertThat(page.locator("table")).containsText("project_manager");
    }
}
