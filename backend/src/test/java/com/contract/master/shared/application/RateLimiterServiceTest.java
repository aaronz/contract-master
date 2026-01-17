package com.contract.master.shared.application;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RateLimiterServiceTest {

    @Test
    void testTryConsume() {
        RateLimiterService service = new RateLimiterService();
        String key = "TEST_KEY";

        for (int i = 0; i < 100; i++) {
            assertTrue(service.tryConsume(key), "Request " + i + " should be allowed");
        }

        assertFalse(service.tryConsume(key), "Request 101 should be blocked");
    }
}
