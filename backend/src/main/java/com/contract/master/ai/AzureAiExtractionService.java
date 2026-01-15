package com.contract.master.ai;

import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
public class AzureAiExtractionService {

    public Map<String, String> extractElements(byte[] content) {
        Map<String, String> results = new HashMap<>();
        results.put("effective_date", "2024-01-01");
        results.put("amount", "1000000.00");
        return results;
    }
}
