package com.contract.master.e2e.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class ContractListPO {
    private final Page page;
    private final String baseUrl;

    public ContractListPO(Page page, String baseUrl) {
        this.page = page;
        this.baseUrl = baseUrl;
    }

    public ContractDetailPO createContract() {
        page.click("button:has-text('New Contract')");
        return new ContractDetailPO(page, baseUrl);
    }

    public void search(String query) {
        page.fill(".search-input input", query);
        page.waitForResponse(response -> response.url().contains("/api/contracts") && response.url().contains("query="), () -> {
            page.keyboard().press("Enter");
        });
        page.waitForLoadState(com.microsoft.playwright.options.LoadState.NETWORKIDLE);
    }

    public ContractDetailPO openContract(String contractNo) {
        page.locator("tr:has-text('" + contractNo + "')").first().click();
        return new ContractDetailPO(page, baseUrl);
    }

    public int getRowCount() {
        return page.locator("tr.el-table__row").count();
    }
}
