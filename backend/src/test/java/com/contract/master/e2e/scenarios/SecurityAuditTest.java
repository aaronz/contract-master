package com.contract.master.e2e.scenarios;

import com.contract.master.e2e.E2ETestBase;
import org.junit.jupiter.api.Test;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class SecurityAuditTest extends E2ETestBase {

    @Test
    void testCrossTenantInjectionResistance() {
        login("admin", "password", "tenant-1");
        page.navigate(baseUrl + "/contracts");
        page.waitForSelector(".el-table__row");
        
        page.click(".el-table__row:nth-child(1) button");
        page.waitForURL(url -> url.contains("/contracts/"));
        
        page.evaluate("() => localStorage.setItem('tenantId', 'tenant-evil')");
        page.reload();
        
        page.waitForSelector(".tenant-name");
        assertThat(page.locator(".tenant-name")).not().hasText("Evil Corp");
    }

}
