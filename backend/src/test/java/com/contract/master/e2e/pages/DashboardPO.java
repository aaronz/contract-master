package com.contract.master.e2e.pages;

import com.microsoft.playwright.Page;

public class DashboardPO {
    private final Page page;
    private final String baseUrl;

    public DashboardPO(Page page, String baseUrl) {
        this.page = page;
        this.baseUrl = baseUrl;
    }

    public ContractListPO navigateToContracts() {
        page.click("text=Contract Management");
        return new ContractListPO(page, baseUrl);
    }

    public ProblemCenterPO navigateToRisk() {
        page.click("text=Problem Center");
        return new ProblemCenterPO(page, baseUrl);
    }

    public RuleManagementPO navigateToRules() {
        page.click(".el-sub-menu__title:has-text('Rule Engine')");
        page.click(".el-menu-item:has-text('Evaluation Rules')");
        return new RuleManagementPO(page, baseUrl);
    }
}
