package com.contract.master.e2e.scenarios;

import com.contract.master.e2e.E2ETestBase;
import com.contract.master.e2e.pages.LoginPage;
import com.contract.master.e2e.pages.SettingsPage;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class US4ConfigurationTest extends E2ETestBase {

    @Test
    void testDynamicFieldConfiguration() {
        LoginPage loginPage = new LoginPage(page);
        loginPage.login("admin", "password", "tenant-1");
        
        page.navigate("http://localhost:5173/settings/fields");
        SettingsPage settingsPage = new SettingsPage(page);
        settingsPage.addCustomField("Project Code", "TEXT");
        
        page.navigate("http://localhost:5173/contract/detail/1");
        assertTrue(page.isVisible("input[name=\"custom_field_project_code\"]"));
    }
}
