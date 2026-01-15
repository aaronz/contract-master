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
        page.fill("input[name=\"tenantId\"]", tenantId);
        page.click("button.login-submit");
        return new DashboardPage(page);
    }

    public boolean hasErrorMessage() {
        return page.isVisible(".el-message--error");
    }
}
