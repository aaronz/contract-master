package com.contract.master.e2e.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class ProblemCenterPO {
    private final Page page;
    private final String baseUrl;

    public ProblemCenterPO(Page page, String baseUrl) {
        this.page = page;
        this.baseUrl = baseUrl;
    }

    public int getProblemCount() {
        return page.locator("tr.el-table__row").count();
    }

    public void selectContract(String contractNo) {
        page.locator(".header-right .el-select").first().click();
        page.waitForSelector(".el-select-dropdown:visible", new Page.WaitForSelectorOptions().setTimeout(10000));
        
        // Wait for the specific contract item to appear in the dropdown
        Locator item = page.locator(".el-select-dropdown__item").filter(new Locator.FilterOptions().setHasText(contractNo));
        item.first().waitFor();
        item.first().click();
    }

    public void runRules() {
        page.waitForResponse(response -> response.url().contains("/api/evaluations") && response.request().method().equals("POST"), () -> {
            page.click("button:has-text('Run Rules')");
        });
        page.waitForSelector("text=Evaluation polling completed", new Page.WaitForSelectorOptions().setTimeout(60000));
    }

    public void resolveProblem(String problemId) {
        page.locator("tr:has-text('" + problemId + "')").locator("button:has-text('Resolve')").click();
        page.click("button:has-text('Confirm')");
        page.waitForSelector("text=successfully");
    }
}
