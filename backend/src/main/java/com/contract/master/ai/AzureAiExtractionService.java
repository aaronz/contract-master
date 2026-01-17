package com.contract.master.ai;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class AzureAiExtractionService {

    private static final Logger logger = LoggerFactory.getLogger(AzureAiExtractionService.class);
    
    private static final Pattern AMOUNT_PATTERN = Pattern.compile("Amount[:\\s]+([0-9,.]+)", Pattern.CASE_INSENSITIVE);
    private static final Pattern DATE_PATTERN = Pattern.compile("Date[:\\s]+(\\d{4}-\\d{2}-\\d{2})", Pattern.CASE_INSENSITIVE);

    public Map<String, String> extractElements(byte[] content) {
        if (content == null || content.length == 0) {
            logger.warn("Received empty content for AI extraction");
            throw new IllegalArgumentException("Content cannot be null or empty");
        }

        Map<String, String> results = new HashMap<>();
        
        try {
            String textContent = new String(content, StandardCharsets.UTF_8);
            
            Matcher amountMatcher = AMOUNT_PATTERN.matcher(textContent);
            if (amountMatcher.find()) {
                results.put("amount", amountMatcher.group(1));
            }

            Matcher dateMatcher = DATE_PATTERN.matcher(textContent);
            if (dateMatcher.find()) {
                results.put("effective_date", dateMatcher.group(1));
            }
            
            logger.info("Extracted {} elements from content", results.size());
        } catch (Exception e) {
            logger.error("Error parsing content", e);
            throw new RuntimeException("Failed to process content extraction", e);
        }

        return results;
    }
}
