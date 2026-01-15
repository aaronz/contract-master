package com.contract.master.e2e.scenarios;

import com.contract.master.e2e.E2ETestBase;
import com.contract.master.e2e.pages.LoginPage;
import com.contract.master.e2e.pages.DashboardPage;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class US1AuthTest extends E2ETestBase {

    @Test
    void testSuccessfulLogin() {
        login("admin", "password", "tenant-1");
        assertTrue(page.url().contains("/dashboard") || page.locator(".dashboard-container").first().isVisible());
    }

    @Test
    void testInvalidLogin() {
        page.navigate(baseUrl + "/login");
        LoginPage loginPage = new LoginPage(page);
        loginPage.login("admin", "wrong-password", "tenant-1");
        assertTrue(loginPage.hasErrorMessage());
    }
}
