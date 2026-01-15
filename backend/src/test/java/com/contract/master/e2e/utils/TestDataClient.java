package com.contract.master.e2e.utils;

import com.contract.master.dto.ContractDTO;
import java.util.Map;
import java.util.HashMap;

public class TestDataClient {
    public void setupTenant(String tenantId) {
    }

    public void injectContract(String tenantId, String scenario) {
    }

    public static Map<String, Object> getMockPayload(String scenario) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("id", "CRM-" + System.currentTimeMillis());
        payload.put("name", "E2E Test Contract " + scenario);
        payload.put("number", "CON-" + System.currentTimeMillis());
        payload.put("amount", "50000.00");
        payload.put("party_a", "Company A");
        payload.put("party_b", "Company B");
        return payload;
    }
}
