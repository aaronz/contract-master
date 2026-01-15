package com.contract.master.e2e.pages;

import com.microsoft.playwright.Page;

public class SettingsPage {
    private final Page page;

    public SettingsPage(Page page) {
        this.page = page;
    }

    public void addCustomField(String name, String type) {
        page.click("button:has-text(\"New Field\")");
        page.fill("input[name=\"field_name\"]", name);
        page.selectOption("select[name=\"field_type\"]", type);
        page.click("button.save-field");
    }

    public void toggleRule(String ruleName, boolean enable) {
    }
}
