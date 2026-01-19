package com.contract.master.contract.infrastructure.persistence;

import com.contract.master.contract.domain.model.ContractId;
import com.contract.master.contract.domain.model.ContractNo;
import com.contract.master.shared.domain.model.TenantId;
import com.contract.master.contract.domain.model.Contract;
import com.contract.master.contract.domain.repository.ContractRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.List;

@Repository
public interface JpaContractRepository extends JpaRepository<Contract, Long>, ContractRepository {
    @Override
    @Query("SELECT c FROM Contract c WHERE c.contractId = :id")
    Optional<Contract> findById(ContractId id);

    @Override
    Optional<Contract> findByContractNo(ContractNo contractNo);

    @Override
    Optional<Contract> findByCrmId(String crmId);

    @Override
    @Query("SELECT COUNT(c) FROM Contract c WHERE c.tenantId.id = :#{#tenantId.id}")
    Long countByTenantId(TenantId tenantId);

    @Override
    @Query("SELECT COUNT(c) FROM Contract c WHERE c.tenantId.id = :#{#tenantId.id} AND c.approvalStatus = 'PENDING'")
    Long countPendingApprovalsByTenantId(TenantId tenantId);

    @Override
    @Query("SELECT SUM(c.amount.amount) FROM Contract c WHERE c.tenantId.id = :#{#tenantId.id} AND c.status = 'PUBLISHED'")
    BigDecimal sumActiveValueByTenantId(TenantId tenantId);

    @Override
    @Query("SELECT COUNT(c) FROM Contract c WHERE c.tenantId.id = :#{#tenantId.id} AND c.status = 'RISK_FLAGGED'")
    Long countRiskAlertsByTenantId(TenantId tenantId);

    @Override
    @Query("SELECT c FROM Contract c WHERE c.tenantId.id = :#{#tenantId.id}")
    Page<Contract> findByTenantId(TenantId tenantId, Pageable pageable);

    @Override
    @Query("SELECT c FROM Contract c WHERE c.tenantId.id = :#{#tenantId.id}")
    List<Contract> findByTenantId(TenantId tenantId);

    @Override
    @Query("SELECT c FROM Contract c WHERE c.tenantId.id = :#{#tenantId.id} AND (c.contractName LIKE %:query% OR c.contractNo.value LIKE %:query%)")
    Page<Contract> findByTenantIdAndQuery(TenantId tenantId, String query, Pageable pageable);

    @Override
    @Query("SELECT new com.contract.master.dashboard.dto.DashboardStatsDTO$DailyCountDTO(CAST(c.createTime as string), COUNT(c)) " +
           "FROM Contract c WHERE c.tenantId.id = :#{#tenantId.id} " +
           "GROUP BY CAST(c.createTime as string)")
    List<com.contract.master.dashboard.dto.DashboardStatsDTO.DailyCountDTO> getVolumeTrendByTenantId(TenantId tenantId);
}
