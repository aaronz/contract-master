package com.contract.master.e2e.utils;

import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.RequestOptions;

import java.util.HashMap;
import java.util.Map;

public class TestDataGenerator {
    private final APIRequestContext requestContext;
    private final String baseUrl;

    public TestDataGenerator(Playwright playwright) {
        this.baseUrl = System.getProperty("backend.url", "http://localhost:8080");
        this.requestContext = playwright.request().newContext(new APIRequest.NewContextOptions()
                .setBaseURL(baseUrl));
    }

    public void seedContract(String tenantId, Map<String, Object> data) {
        requestContext.post("/api/contracts", RequestOptions.create()
                .setHeader("X-Tenant-ID", tenantId)
                .setData(data));
    }

    public void seedUser(String username, String password, String tenantId) {
        Map<String, String> data = new HashMap<>();
        data.put("username", username);
        data.put("password", password);
        data.put("tenantId", tenantId);
        
        requestContext.post("/api/users", RequestOptions.create().setData(data));
    }
}
