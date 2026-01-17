package com.contract.master.dashboard.application;

import com.contract.master.contract.domain.repository.ContractBaseRepository;
import com.contract.master.dto.DashboardStatsDTO;
import com.contract.master.security.TenantContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class DashboardService {

    @Autowired
    private ContractBaseRepository contractBaseRepository;

    public DashboardStatsDTO getStats() {
        String tenantId = TenantContext.getCurrentTenant();
        DashboardStatsDTO stats = new DashboardStatsDTO();
        
        stats.setTotalContracts(contractBaseRepository.countByTenantId(tenantId));
        stats.setPendingApprovals(contractBaseRepository.countPendingApprovalsByTenantId(tenantId));
        BigDecimal activeValue = contractBaseRepository.sumActiveValueByTenantId(tenantId);
        stats.setActiveValue(activeValue != null ? activeValue : BigDecimal.ZERO);
        stats.setRiskAlerts(contractBaseRepository.countRiskAlertsByTenantId(tenantId));

        List<DashboardStatsDTO.DailyCountDTO> trend = new ArrayList<>();
        trend.add(new DashboardStatsDTO.DailyCountDTO("Mon", 10L));
        trend.add(new DashboardStatsDTO.DailyCountDTO("Tue", 15L));
        trend.add(new DashboardStatsDTO.DailyCountDTO("Wed", 8L));
        trend.add(new DashboardStatsDTO.DailyCountDTO("Thu", 20L));
        trend.add(new DashboardStatsDTO.DailyCountDTO("Fri", 25L));
        stats.setVolumeTrend(trend);

        stats.setRiskRadar(Arrays.asList(
            new DashboardStatsDTO.RadarStatDTO("Financial", 40),
            new DashboardStatsDTO.RadarStatDTO("Legal", 20),
            new DashboardStatsDTO.RadarStatDTO("Compliance", 60),
            new DashboardStatsDTO.RadarStatDTO("Operational", 30),
            new DashboardStatsDTO.RadarStatDTO("Performance", 50)
        ));

        return stats;
    }
}
