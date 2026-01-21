package com.contract.master.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ThreadContextPropagationTest {

    @Autowired
    private AsyncTestService asyncTestService;

    @Test
    void shouldPropagateTenantContextToAsyncThread() throws Exception {
        // Given
        String tenantId = "tenant-propagation-test";
        TenantContext.setCurrentTenant(tenantId);

        // When
        CompletableFuture<String> future = asyncTestService.getTenantInAsyncThread();
        String result = future.get(5, TimeUnit.SECONDS);

        // Then
        assertThat(result).isEqualTo(tenantId);
        
        TenantContext.clear();
    }
}
