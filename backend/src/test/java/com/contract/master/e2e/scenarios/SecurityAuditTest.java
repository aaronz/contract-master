package com.contract.master.e2e.scenarios;

import com.contract.master.e2e.E2ETestBase;
import org.junit.jupiter.api.Test;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class SecurityAuditTest extends E2ETestBase {

    @Test
    void testCrossTenantInjectionResistance() {
        login("admin", "password", "tenant-1");
        page.navigate(baseUrl + "/contract/detail/1");
        page.evaluate("() => localStorage.setItem('tenantId', 'tenant-evil')");
        page.reload();
        assertThat(page.locator(".tenant-name")).not().hasText("Evil Corp");
    }
}
