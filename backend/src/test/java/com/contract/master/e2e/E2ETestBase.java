package com.contract.master.e2e;

import com.contract.master.e2e.utils.BrowserFactory;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.TestInfo;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class E2ETestBase {
    protected Playwright playwright;
    protected Browser browser;
    protected BrowserContext context;
    protected Page page;

    @BeforeAll
    void launchBrowser() {
        playwright = Playwright.create();
        browser = BrowserFactory.createBrowser(playwright);
    }

    @AfterAll
    void closeBrowser() {
        if (browser != null) browser.close();
        if (playwright != null) playwright.close();
    }

    @BeforeEach
    void createContext() {
        context = browser.newContext();
        context.tracing().start(new com.microsoft.playwright.Tracing.StartOptions()
                .setScreenshots(true)
                .setSnapshots(true)
                .setSources(true));
        page = context.newPage();

        // Debugging Hook: Capture Console Logs
        page.onConsoleMessage(msg -> {
            System.out.println("BROWSER CONSOLE [" + msg.type() + "]: " + msg.text());
        });

        // Debugging Hook: Capture Failed Network Requests
        page.onResponse(response -> {
            if (response.status() >= 400) {
                System.err.println("BROWSER NETWORK ERROR [" + response.status() + "]: " + response.url());
            }
        });
    }

    @AfterEach
    void closeContext(TestInfo testInfo) {
        if (context != null) {
            String testName = testInfo.getTestClass().get().getSimpleName() + "_" + 
                             testInfo.getTestMethod().get().getName();
            
            // Debugging Hook: Capture Screenshot on Completion/Failure
            page.screenshot(new Page.ScreenshotOptions()
                    .setPath(java.nio.file.Paths.get("target/screenshots/" + testName + ".png")));

            String tracePath = "target/playwright-traces/" + testName + ".zip";
            
            context.tracing().stop(new com.microsoft.playwright.Tracing.StopOptions()
                    .setPath(java.nio.file.Paths.get(tracePath)));
            context.close();
        }
    }
    
    protected String getBaseUrl() {
        return System.getProperty("frontend.url", "http://localhost:5173");
    }
}
