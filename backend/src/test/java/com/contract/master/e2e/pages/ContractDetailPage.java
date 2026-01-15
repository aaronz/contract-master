package com.contract.master.e2e.pages;

import com.microsoft.playwright.Page;

public class ContractDetailPage {
    private final Page page;

    public ContractDetailPage(Page page) {
        this.page = page;
    }

    public void triggerAiAnalysis() {
        page.click("button:has-text(\"AI Analysis\")", new Page.ClickOptions().setForce(true));
    }

    public boolean isProgressVisible() {
        return page.isVisible(".el-progress");
    }

    public String getFieldValue(String fieldName) {
        return page.inputValue("input[name=\"" + fieldName + "\"]");
    }
}
