package com.contract.master.service;

import com.contract.master.domain.AuditLog;
import com.contract.master.domain.AuditLogRepository;
import com.contract.master.security.TenantContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuditService {

    @Autowired
    private AuditLogRepository auditLogRepository;

    public void logChange(String contractId, String field, String oldVal, String newVal, String type, String user) {
        AuditLog log = new AuditLog();
        log.setContractId(contractId);
        log.setFieldName(field);
        log.setOldValue(oldVal);
        log.setNewValue(newVal);
        log.setModifyType(type);
        log.setModifyUser(user);
        auditLogRepository.save(log);
    }

    public List<AuditLog> getAuditLogsByContract(String contractId) {
        return auditLogRepository.findByContractIdOrderByCreateTimeDesc(contractId);
    }
}
