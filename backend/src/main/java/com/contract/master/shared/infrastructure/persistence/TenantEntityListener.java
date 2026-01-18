package com.contract.master.shared.infrastructure.persistence;

import com.contract.master.security.TenantContext;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import org.springframework.stereotype.Component;
import com.contract.master.shared.domain.model.TenantAware;
import com.contract.master.shared.domain.model.TenantId;

@Component
public class TenantEntityListener {

    @PrePersist
    @PreUpdate
    public void setTenantId(Object entity) {
        if (entity instanceof TenantAware tenantAware) {
            String tenantId = TenantContext.getCurrentTenant();
            if (tenantId != null) {
                tenantAware.setTenantId(TenantId.of(tenantId));
            }
        }
    }
}
