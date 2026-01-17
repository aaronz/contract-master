package com.contract.master.security;

import com.contract.master.audit.domain.model.AuditLog;
import com.contract.master.audit.domain.repository.AuditLogRepository;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.PreRemove;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AuditLogInterceptor {

    private static AuditLogRepository auditLogRepository;

    @Autowired
    public void setAuditLogRepository(AuditLogRepository repository) {
        AuditLogInterceptor.auditLogRepository = repository;
    }

    @PrePersist
    public void onPrePersist(Object entity) {
        logAction(entity, "CREATE");
    }

    @PreUpdate
    public void onPreUpdate(Object entity) {
        logAction(entity, "UPDATE");
    }

    @PreRemove
    public void onPreRemove(Object entity) {
        logAction(entity, "DELETE");
    }

    private void logAction(Object entity, String action) {
        if (entity instanceof AuditLog) return;
        if (auditLogRepository == null) return;

        AuditLog log = new AuditLog();
        log.setModifyType(action);
        log.setModifyUser(com.contract.master.security.TenantContext.getCurrentTenant());
        
        auditLogRepository.save(log);
    }
}
