package com.contract.master.audit.domain.repository;

import com.contract.master.audit.domain.model.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
    List<AuditLog> findByContractIdOrderByCreateTimeDesc(String contractId);
    List<AuditLog> findAllByOrderByCreateTimeDesc();
}
