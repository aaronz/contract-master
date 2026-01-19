package com.contract.master.e2e.utils;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Playwright;

public class BrowserFactory {
    
    public static Browser createBrowser(Playwright playwright) {
        String headlessEnv = System.getenv("PLAYWRIGHT_HEADLESS");
        boolean headless = headlessEnv == null || Boolean.parseBoolean(headlessEnv);
        
        // Using Firefox as Chromium has issues in this environment
        return playwright.firefox().launch(new BrowserType.LaunchOptions()
                .setHeadless(headless)
                .setSlowMo(headless ? 0 : 500));
    }
}
