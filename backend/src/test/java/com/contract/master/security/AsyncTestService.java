package com.contract.master.security;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class AsyncTestService {

    @Async("taskExecutor")
    public CompletableFuture<String> getTenantInAsyncThread() {
        return CompletableFuture.completedFuture(TenantContext.getCurrentTenant());
    }
}
