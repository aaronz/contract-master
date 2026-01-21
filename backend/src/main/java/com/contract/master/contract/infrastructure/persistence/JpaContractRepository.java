package com.contract.master.contract.infrastructure.persistence;

import com.contract.master.contract.domain.model.ContractId;
import com.contract.master.contract.domain.model.ContractNo;
import com.contract.master.shared.domain.model.TenantId;
import com.contract.master.contract.domain.model.Contract;
import com.contract.master.contract.domain.repository.ContractRepository;
import com.contract.master.dashboard.dto.DashboardStatsDTO;
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
    Page<Contract> findAll(Pageable pageable);
    
    @Override
    List<Contract> findAll();

    @Override
    @Query("SELECT COUNT(c) FROM Contract c WHERE c.approvalStatus = 'PENDING'")
    Long countPendingApprovals();
    
    @Override
    @Query("SELECT SUM(c.amount.amount) FROM Contract c WHERE c.status = 'PUBLISHED'")
    BigDecimal sumActiveValue();
    
    @Override
    @Query("SELECT COUNT(c) FROM Contract c WHERE c.status = 'RISK_FLAGGED'")
    Long countRiskAlerts();

    @Override
    @Query("SELECT new com.contract.master.dashboard.dto.DashboardStatsDTO$DailyCountDTO(CAST(c.createTime as string), COUNT(c)) " +
           "FROM Contract c " +
           "GROUP BY CAST(c.createTime as string) " +
           "ORDER BY CAST(c.createTime as string) ASC")
    List<DashboardStatsDTO.DailyCountDTO> getVolumeTrend();
}
