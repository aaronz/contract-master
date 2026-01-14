package com.contract.master.repository;

import com.contract.master.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
    List<AuditLog> findByContractIdOrderByCreateTimeDesc(String contractId);
}
