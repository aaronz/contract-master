package com.contract.master.e2e.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import java.util.Map;
import java.util.List;

public class RuleManagementPO {
    private final Page page;
    private final String baseUrl;

    public RuleManagementPO(Page page, String baseUrl) {
        this.page = page;
        this.baseUrl = baseUrl;
    }

    public void createRule(Map<String, String> data) {
        page.click("button:has-text('Add New Rule')");
        data.forEach((label, value) -> {
            Locator field = page.locator("label:has-text('" + label + "')").locator("xpath=..").locator("input, textarea").first();
            if (field.count() > 0) {
                field.fill(value);
            }
        });
        
        page.waitForResponse(response -> response.url().contains("/api/rules") && (response.request().method().equals("POST") || response.request().method().equals("PUT")), () -> {
            page.click("button:has-text('Save Rule')");
        });
        
        page.waitForSelector(".el-message--success", new Page.WaitForSelectorOptions().setTimeout(5000));
        page.waitForSelector(".el-dialog", new Page.WaitForSelectorOptions().setState(com.microsoft.playwright.options.WaitForSelectorState.HIDDEN));
    }

    public List<String> getRuleList() {
        return page.locator("tr.el-table__row td:first-child").allTextContents();
    }
}
