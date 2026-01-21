package com.contract.master.config;

import com.contract.master.security.TenantContext;
import org.springframework.core.task.TaskDecorator;

public class ContextCopyingDecorator implements TaskDecorator {
    @Override
    public Runnable decorate(Runnable runnable) {
        String tenantId = TenantContext.getCurrentTenant();
        return () -> {
            try {
                TenantContext.setCurrentTenant(tenantId);
                runnable.run();
            } finally {
                TenantContext.clear();
            }
        };
    }
}
