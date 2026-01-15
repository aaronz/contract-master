package com.contract.master.e2e.pages;

import com.microsoft.playwright.Page;

public class ContractListPage {
    private final Page page;

    public ContractListPage(Page page) {
        this.page = page;
    }

    public void search(String contractNo) {
        page.fill(".search-input input", contractNo);
    }

    public ContractDetailPage viewFirstDetail() {
        page.locator(".el-table__row").first().locator("button").first().click();
        return new ContractDetailPage(page);
    }
}
