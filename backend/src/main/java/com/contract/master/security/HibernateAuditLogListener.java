package com.contract.master.security;

import com.contract.master.audit.domain.model.AuditLog;
import com.contract.master.audit.domain.repository.AuditLogRepository;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.*;
import org.hibernate.persister.entity.EntityPersister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManagerFactory;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
public class HibernateAuditLogListener implements PostCommitUpdateEventListener {

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Autowired
    private AuditLogRepository auditLogRepository;

    @PostConstruct
    protected void init() {
        org.hibernate.engine.spi.SessionFactoryImplementor sessionFactory = entityManagerFactory.unwrap(org.hibernate.engine.spi.SessionFactoryImplementor.class);
        EventListenerRegistry registry = sessionFactory.getServiceRegistry().getService(EventListenerRegistry.class);
        registry.getEventListenerGroup(EventType.POST_COMMIT_UPDATE).appendListener(this);
    }

    @Override
    public void onPostUpdate(PostUpdateEvent event) {
        Object entity = event.getEntity();
        if (entity instanceof AuditLog) return;

        String[] propertyNames = event.getPersister().getPropertyNames();
        Object[] oldState = event.getOldState();
        Object[] newState = event.getState();
        int[] dirtyProperties = event.getDirtyProperties();

        if (dirtyProperties == null) return;

        Map<String, Map<String, Object>> diff = new HashMap<>();
        for (int index : dirtyProperties) {
            String propertyName = propertyNames[index];
            Object oldVal = oldState[index];
            Object newVal = newState[index];

            if (!Objects.equals(oldVal, newVal)) {
                Map<String, Object> values = new HashMap<>();
                values.put("old", oldVal != null ? oldVal.toString() : null);
                values.put("new", newVal != null ? newVal.toString() : null);
                diff.put(propertyName, values);
            }
        }

        if (!diff.isEmpty()) {
            AuditLog log = new AuditLog();
            log.setModifyType("UPDATE");
            log.setModifyUser(TenantContext.getCurrentTenant());
            log.setDetails(diff.toString());
            auditLogRepository.save(log);
        }
    }

    @Override
    public void onPostUpdateCommitFailed(PostUpdateEvent event) {
    }

    @Override
    public boolean requiresPostCommitHandling(EntityPersister persister) {
        return true;
    }
}
