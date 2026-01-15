package com.contract.master.e2e.pages;

import com.microsoft.playwright.Page;

public class LoginPage {
    private final Page page;

    public LoginPage(Page page) {
        this.page = page;
    }

    public DashboardPage login(String username, String password, String tenantId) {
        page.fill("input[name=\"username\"]", username);
        page.fill("input[name=\"password\"]", password);
        page.click("button.login-button");
        return new DashboardPage(page);
    }

    public boolean hasErrorMessage() {
        try {
            page.waitForSelector(".el-message--error", new Page.WaitForSelectorOptions().setTimeout(5000));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
