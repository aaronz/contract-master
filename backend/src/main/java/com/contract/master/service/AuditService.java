package com.contract.master.service;

import com.contract.master.domain.AuditLog;
import com.contract.master.domain.AuditLogRepository;
import com.contract.master.security.TenantContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class AuditService {

    public static final String RE_EVALUATION_TRIGGERED = "RE_EVALUATION_TRIGGERED";
    public static final String MANUAL = "MANUAL"; // Assuming MANUAL is a common type

    @Autowired
    private AuditLogRepository auditLogRepository;

    // Constructor for dependency injection
    public AuditService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    public void logChange(String contractId, String field, String oldVal, String newVal, String type, String user) {
        AuditLog log = new AuditLog();
        log.setContractId(contractId);
        log.setFieldName(field);
        log.setOldValue(oldVal);
        log.setNewValue(newVal);
        log.setModifyType(type);
        log.setModifyUser(user);
        log.setTenantId(TenantContext.getCurrentTenant()); // Ensure tenantId is set
        auditLogRepository.save(log);
    }

    public void logReEvaluationTriggered(String contractId, String ruleIds, String user) {
        AuditLog log = new AuditLog();
        log.setContractId(contractId);
        log.setFieldName("re-evaluation"); // Or more specific field
        log.setNewValue("Rules: " + ruleIds); // Details about the re-evaluation
        log.setModifyType(RE_EVALUATION_TRIGGERED);
        log.setModifyUser(user);
        log.setTenantId(TenantContext.getCurrentTenant()); // Ensure tenantId is set
        auditLogRepository.save(log);
    }

    public List<AuditLog> getAuditLogsByContract(String contractId) {
        return auditLogRepository.findByContractIdOrderByCreateTimeDesc(contractId);
    }
}
