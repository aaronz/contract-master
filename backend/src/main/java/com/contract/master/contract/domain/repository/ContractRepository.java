package com.contract.master.contract.domain.repository;

import com.contract.master.contract.domain.model.Contract;
import com.contract.master.contract.domain.model.ContractId;
import com.contract.master.contract.domain.model.ContractNo;
import com.contract.master.shared.domain.model.TenantId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ContractRepository {
    Optional<Contract> findById(ContractId id);
    Contract save(Contract contract);
    Page<Contract> findAll(Pageable pageable);
    List<Contract> findAll();
    
    Optional<Contract> findByContractNo(ContractNo contractNo);

    long count();
    
    @org.springframework.data.jpa.repository.Query("SELECT COUNT(c) FROM Contract c WHERE c.approvalStatus = 'PENDING'")
    Long countPendingApprovals();
    
    @org.springframework.data.jpa.repository.Query("SELECT SUM(c.amount.amount) FROM Contract c WHERE c.status = 'PUBLISHED'")
    BigDecimal sumActiveValue();
    
    @org.springframework.data.jpa.repository.Query("SELECT COUNT(c) FROM Contract c WHERE c.status = 'RISK_FLAGGED'")
    Long countRiskAlerts();

    @org.springframework.data.jpa.repository.Query("SELECT new com.contract.master.dashboard.dto.DashboardStatsDTO$DailyCountDTO(CAST(c.createTime as string), COUNT(c)) " +
           "FROM Contract c " +
           "GROUP BY CAST(c.createTime as string) " +
           "ORDER BY CAST(c.createTime as string) ASC")
    List<com.contract.master.dashboard.dto.DashboardStatsDTO.DailyCountDTO> getVolumeTrend();
}
