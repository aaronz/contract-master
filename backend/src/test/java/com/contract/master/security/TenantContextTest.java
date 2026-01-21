package com.contract.master.security;

import com.contract.master.shared.domain.model.TenantId;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;

public class TenantContextTest {

    @Test
    void shouldExecuteAsSystemAndRestoreContext() {
        // Given
        String initialTenant = "tenant-1";
        TenantContext.setCurrentTenant(initialTenant);
        AtomicReference<String> capturedTenant = new AtomicReference<>();

        // When
        TenantContext.executeAsSystem(() -> {
            capturedTenant.set(TenantContext.getCurrentTenant());
        });

        // Then
        assertThat(capturedTenant.get()).isNull();
        assertThat(TenantContext.getCurrentTenant()).isEqualTo(initialTenant);
        
        TenantContext.clear();
    }

    @Test
    void shouldHandleNullInitialContextInExecuteAsSystem() {
        // Given
        TenantContext.clear();
        AtomicReference<String> capturedTenant = new AtomicReference<>();

        // When
        TenantContext.executeAsSystem(() -> {
            capturedTenant.set(TenantContext.getCurrentTenant());
        });

        // Then
        assertThat(capturedTenant.get()).isNull();
        assertThat(TenantContext.getCurrentTenant()).isNull();
    }
}
