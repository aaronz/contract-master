package com.contract.master.e2e;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;

public class E2ETestBase {
    protected static Playwright playwright;
    protected static Browser browser;
    protected BrowserContext context;
    protected Page page;

    @BeforeAll
    static void launchBrowser() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));
    }

    @AfterAll
    static void closeBrowser() {
        if (playwright != null) {
            playwright.close();
        }
    }

    @BeforeEach
    void createContext() {
        context = browser.newContext();
        page = context.newPage();
    }

    @AfterEach
    void closeContext() {
        if (page != null && page.context() != null) {
            page.screenshot(new Page.ScreenshotOptions().setPath(java.nio.file.Paths.get("target/test-failure-" + System.currentTimeMillis() + ".png")));
        }
        if (context != null) {
            context.close();
        }
    }

    protected BrowserContext createTenantContext(String tenantId) {
        return browser.newContext();
    }
}
