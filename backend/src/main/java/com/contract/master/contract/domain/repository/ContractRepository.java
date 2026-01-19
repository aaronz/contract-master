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
    Page<Contract> findByTenantId(TenantId tenantId, Pageable pageable);
    
    Optional<Contract> findByContractNo(ContractNo contractNo);
    Optional<Contract> findByCrmId(String crmId);
    List<Contract> findByTenantId(TenantId tenantId);
    Long countByTenantId(TenantId tenantId);
    Long countPendingApprovalsByTenantId(TenantId tenantId);
    BigDecimal sumActiveValueByTenantId(TenantId tenantId);
    Long countRiskAlertsByTenantId(TenantId tenantId);
    long count();

    Page<Contract> findByTenantIdAndQuery(TenantId tenantId, String query, Pageable pageable);

    @org.springframework.data.jpa.repository.Query("SELECT new com.contract.master.dashboard.dto.DashboardStatsDTO$DailyCountDTO(FUNCTION('DATE_FORMAT', c.createTime, '%Y-%m-%d'), COUNT(c)) " +
           "FROM Contract c WHERE c.tenantId.id = :#{#tenantId.id} " +
           "GROUP BY FUNCTION('DATE_FORMAT', c.createTime, '%Y-%m-%d') " +
           "ORDER BY FUNCTION('DATE_FORMAT', c.createTime, '%Y-%m-%d') ASC")
    List<com.contract.master.dashboard.dto.DashboardStatsDTO.DailyCountDTO> getVolumeTrendByTenantId(TenantId tenantId);
}
