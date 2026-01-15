package com.contract.master.e2e.scenarios;

import com.contract.master.e2e.E2ETestBase;
import org.junit.jupiter.api.Test;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class US4ConfigurationTest extends E2ETestBase {

    @Test
    void testFullFieldModelVisibility() {
        login("admin", "password", "tenant-1");
        
        page.navigate(baseUrl + "/settings/fields");
        page.waitForSelector(".draggable-table");
        assertThat(page.locator(".draggable-table")).containsText("contractNo");
        assertThat(page.locator(".draggable-table")).containsText("amount");
    }

    @Test
    void testMaskingRuleFieldSelection() {
        login("admin", "password", "tenant-1");
        page.navigate(baseUrl + "/compliance/masking");
        
        page.click("button:has-text('Add Masking Rule')");
        page.click(".el-table__row:last-child .el-select");
        assertThat(page.locator(".el-select-dropdown:visible").first()).containsText("Contract Number");
    }
}
