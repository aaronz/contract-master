package com.contract.master.e2e;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;

import com.microsoft.playwright.options.RequestOptions;

public class E2ETestBase {
    protected static Playwright playwright;
    protected static Browser browser;
    protected BrowserContext context;
    protected Page page;
    protected String baseUrl = System.getProperty("test.baseUrl", "http://localhost:5173");

    @BeforeAll
    static void launchBrowser() {
        playwright = Playwright.create();
        browser = playwright.firefox().launch(new BrowserType.LaunchOptions().setHeadless(true));
    }

    @AfterAll
    static void closeBrowser() {
        if (browser != null) {
            browser.close();
        }
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

    protected void login(String username, String password, String tenantId) {
        page.navigate(baseUrl + "/login");
        page.waitForSelector("input[name='username']", new Page.WaitForSelectorOptions().setTimeout(10000));
        page.fill("input[name='username']", username);
        page.fill("input[name='password']", password);
        page.fill("input[name='tenantId']", tenantId);
        page.click("button.login-button");
        page.waitForURL(url -> !url.contains("/login"), new Page.WaitForURLOptions().setTimeout(30000));
    }

    protected String getToken() {
        return (String) page.evaluate("() => localStorage.getItem('token')");
    }

    protected APIResponse getAuthorized(String url) {
        return page.context().request().get(url, RequestOptions.create()
            .setHeader("Authorization", "Bearer " + getToken()));
    }
}
