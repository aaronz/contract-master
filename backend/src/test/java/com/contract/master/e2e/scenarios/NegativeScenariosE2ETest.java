package com.contract.master.e2e.scenarios;

import com.contract.master.e2e.E2ETestBase;
import com.microsoft.playwright.APIResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Negative Scenario E2E Tests")
public class NegativeScenariosE2ETest extends E2ETestBase {

    @Test
    @DisplayName("Security: Access Publish API with Invalid Key")
    void testInvalidAccessKey() {
        APIResponse response = page.context().request().get(baseUrl.replace("5173", "8080") + "/api/publish/contracts?accessKey=invalid-key");
        assertTrue(response.status() == 403 || response.status() == 500);
    }

    @Test
    @DisplayName("Auth: Redirect to Login when Unauthenticated")
    void testUnauthenticatedRedirect() {
        page.navigate(baseUrl + "/contracts");
        assertThat(page).hasURL(baseUrl + "/login");
    }

    @Test
    @DisplayName("Security: Access Protected API without Token")
    void testProtectedApiAccess() {
        APIResponse response = page.context().request().get(baseUrl.replace("5173", "8080") + "/api/contracts");
        assertEquals(403, response.status());
    }

    @Test
    @DisplayName("Data: Access Audit Logs for Non-existent Contract")
    void testAuditLogForMissingContract() {
        login("admin", "password", "tenant-1");
        APIResponse response = getAuthorized(baseUrl.replace("5173", "8080") + "/api/contracts/NON-EXISTENT/audit");
        assertTrue(response.ok());
        assertEquals("{\"status\":200,\"message\":\"Success\",\"data\":[]}", response.text().replaceAll("\\s", ""));
    }
}
