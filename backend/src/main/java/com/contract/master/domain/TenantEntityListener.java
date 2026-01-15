package com.contract.master.domain;

import com.contract.master.security.TenantContext;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import org.springframework.stereotype.Component;

@Component
public class TenantEntityListener {

    @PrePersist
    @PreUpdate
    public void setTenantId(Object entity) {
        if (entity instanceof TenantAware tenantAware) {
            String tenantId = TenantContext.getCurrentTenant();
            if (tenantId != null) {
                tenantAware.setTenantId(tenantId);
            }
        }
    }
}
