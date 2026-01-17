package com.contract.master.contract.domain.repository;

import com.contract.master.contract.domain.model.ContractBase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface ContractBaseRepository extends JpaRepository<ContractBase, String> {
    Optional<ContractBase> findByContractNo(String contractNo);
    Optional<ContractBase> findByCrmId(String crmId);
    Page<ContractBase> findByTenantId(String tenantId, Pageable pageable);
    java.util.List<ContractBase> findByTenantId(String tenantId);

    @Query("SELECT COUNT(c) FROM ContractBase c WHERE c.tenantId = :tenantId")
    Long countByTenantId(String tenantId);

    @Query("SELECT COUNT(c) FROM ContractBase c WHERE c.tenantId = :tenantId AND c.approvalStatus = 'PENDING'")
    Long countPendingApprovalsByTenantId(String tenantId);

    @Query("SELECT SUM(c.amount) FROM ContractBase c WHERE c.tenantId = :tenantId AND c.status = 'PUBLISHED'")
    BigDecimal sumActiveValueByTenantId(String tenantId);

    @Query("SELECT COUNT(c) FROM ContractBase c WHERE c.tenantId = :tenantId AND c.status = 'RISK_FLAGGED'")
    Long countRiskAlertsByTenantId(String tenantId);
}
