package com.contract.master.e2e.pages;

import com.microsoft.playwright.Page;

public class LoginPO {
    private final Page page;
    private final String baseUrl;

    public LoginPO(Page page, String baseUrl) {
        this.page = page;
        this.baseUrl = baseUrl;
    }

    public void navigate() {
        page.navigate(baseUrl + "/login");
    }

    public DashboardPO login(String username, String password, String tenantId) {
        page.fill("input[name='username']", username);
        page.fill("input[name='password']", password);
        page.fill("input[name='tenantId']", tenantId);
        page.click(".login-button");
        page.waitForURL("**/dashboard", new Page.WaitForURLOptions().setTimeout(1000));
        return new DashboardPO(page, baseUrl);
    }

    public String getErrorMessage() {
        return page.textContent(".el-message--error");
    }
}
