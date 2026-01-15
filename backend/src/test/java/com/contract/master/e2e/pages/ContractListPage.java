package com.contract.master.e2e.pages;

import com.microsoft.playwright.Page;

public class ContractListPage {
    private final Page page;

    public ContractListPage(Page page) {
        this.page = page;
    }

    public void search(String contractNo) {
        page.fill("input.contract-search", contractNo);
    }

    public ContractDetailPage viewFirstDetail() {
        page.click("button.action-view:nth-child(1)");
        return new ContractDetailPage(page);
    }
}
