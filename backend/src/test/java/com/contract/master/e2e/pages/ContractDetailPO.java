package com.contract.master.e2e.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import java.util.Map;

public class ContractDetailPO {
    private final Page page;
    private final String baseUrl;

    public ContractDetailPO(Page page, String baseUrl) {
        this.page = page;
        this.baseUrl = baseUrl;
    }

    public void switchToEditMode() {
        page.waitForSelector(".mode-toggle", new Page.WaitForSelectorOptions().setState(com.microsoft.playwright.options.WaitForSelectorState.VISIBLE));
        page.click(".mode-toggle span:has-text('Edit')");
        page.waitForSelector("input", new Page.WaitForSelectorOptions().setState(com.microsoft.playwright.options.WaitForSelectorState.VISIBLE));
    }

    public void fillForm(Map<String, String> data) {
        data.forEach((label, value) -> {
            // Try different ways to find the field group
            Locator groups = page.locator(".field-group").filter(new Locator.FilterOptions().setHasText(java.util.regex.Pattern.compile("^" + label + "$|^" + label + ":?$", java.util.regex.Pattern.CASE_INSENSITIVE)));
            
            if (groups.count() == 0) {
                groups = page.locator(".field-group:has-text('" + label + "')");
            }

            int count = groups.count();
            for (int i = 0; i < count; i++) {
                Locator field = groups.nth(i).locator("input, textarea, .el-input__inner").first();
                if (field.isVisible() && field.isEnabled()) {
                    field.fill(value);
                }
            }
        });
    }

    public void save() {
        Locator createBtn = page.locator("button:has-text('Create')");
        Locator saveBtn = page.locator(".save-btn:visible, button:has-text('Save Changes'):visible");
        
        page.waitForResponse(response -> response.url().contains("/api/contracts") && (response.request().method().equals("POST") || response.request().method().equals("PUT")), () -> {
            if (createBtn.isVisible()) {
                createBtn.click();
            } else if (saveBtn.count() > 0) {
                saveBtn.first().click();
            } else {
                page.click("button:has-text('Save')");
            }
        });
        
        // Final UI stability check: wait for message to appear or dialog to close
        page.waitForSelector(".el-message--success", new Page.WaitForSelectorOptions().setTimeout(5000));
    }

    public String getFieldValue(String label) {
        Locator group = page.locator(".field-group:has-text('" + label + "')").first();
        Locator displayVal = group.locator(".display-val");
        if (displayVal.count() > 0) {
            return displayVal.textContent().trim();
        }
        return group.textContent().trim();
    }

    public void triggerAIAnalysis() {
        page.click("button:has-text('AI Analysis')");
        page.waitForSelector("text=completed", new Page.WaitForSelectorOptions().setTimeout(6000));
    }
}
