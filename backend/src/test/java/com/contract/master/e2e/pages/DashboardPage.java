package com.contract.master.e2e.pages;

import com.microsoft.playwright.Page;

public class DashboardPage {
    private final Page page;

    public DashboardPage(Page page) {
        this.page = page;
    }

    public boolean isVisible() {
        return page.isVisible(".dashboard-container");
    }

    public String getTenantName() {
        return page.textContent(".tenant-name");
    }
}
