package com.contract.master;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class E2ETestBase {
    static Playwright playwright;
    static Browser browser;
    BrowserContext context;
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

    protected void login(String username, String password) {
        page.navigate(baseUrl + "/login");
        page.waitForSelector("input[name='username']");
        page.fill("input[name='username']", username);
        page.fill("input[name='password']", password);
        page.click("button.login-button");
        page.waitForURL(url -> !url.contains("/login"), new Page.WaitForURLOptions().setTimeout(10000));
    }
}
