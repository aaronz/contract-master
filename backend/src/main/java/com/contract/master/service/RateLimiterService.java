package com.contract.master.service;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class RateLimiterService {

    private final Map<String, TokenBucket> buckets = new ConcurrentHashMap<>();

    private static final long REFILL_PERIOD_MS = 60 * 1000;
    private static final int MAX_TOKENS = 100;

    public boolean tryConsume(String key) {
        TokenBucket bucket = buckets.computeIfAbsent(key, k -> new TokenBucket(MAX_TOKENS, REFILL_PERIOD_MS));
        return bucket.tryConsume();
    }

    private static class TokenBucket {
        private final int capacity;
        private final long refillPeriod;
        private final AtomicInteger tokens;
        private final AtomicLong lastRefillTimestamp;

        public TokenBucket(int capacity, long refillPeriod) {
            this.capacity = capacity;
            this.refillPeriod = refillPeriod;
            this.tokens = new AtomicInteger(capacity);
            this.lastRefillTimestamp = new AtomicLong(System.currentTimeMillis());
        }

        public boolean tryConsume() {
            refill();
            int currentTokens = tokens.get();
            if (currentTokens > 0) {
                int nextValue = tokens.updateAndGet(t -> (t > 0) ? t - 1 : 0);
                return nextValue < currentTokens;
            }
            return false;
        }

        private void refill() {
            long now = System.currentTimeMillis();
            long lastRefill = lastRefillTimestamp.get();
            
            if (now > lastRefill + refillPeriod) {
                // Allow one thread to update timestamp and reset tokens
                if (lastRefillTimestamp.compareAndSet(lastRefill, now)) {
                    tokens.set(capacity);
                }
            }
        }
    }
}
