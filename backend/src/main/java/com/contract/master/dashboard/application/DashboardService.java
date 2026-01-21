package com.contract.master.dashboard.application;

import com.contract.master.shared.domain.model.TenantId;
import com.contract.master.contract.domain.repository.ContractRepository;
import com.contract.master.dashboard.dto.DashboardStatsDTO;
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
    private ContractRepository contractRepository;

    @Autowired
    private com.contract.master.problemcenter.domain.repository.ProblemRepository problemRepository;

    public DashboardStatsDTO getStats() {
        DashboardStatsDTO stats = new DashboardStatsDTO();
        
        stats.setTotalContracts(contractRepository.count());
        stats.setPendingApprovals(contractRepository.countPendingApprovals());
        BigDecimal activeValue = contractRepository.sumActiveValue();
        stats.setActiveValue(activeValue != null ? activeValue : BigDecimal.ZERO);
        stats.setRiskAlerts(contractRepository.countRiskAlerts());

        List<DashboardStatsDTO.DailyCountDTO> trend = contractRepository.getVolumeTrend();
        stats.setVolumeTrend(trend);

        long total = stats.getTotalContracts();
        long risk = stats.getRiskAlerts();
        int riskLevel = total > 0 ? (int)((risk * 100) / total) : 0;

        stats.setRiskRadar(Arrays.asList(
            new DashboardStatsDTO.RadarStatDTO("Financial", 10 + (riskLevel / 2)),
            new DashboardStatsDTO.RadarStatDTO("Legal", 20 + riskLevel),
            new DashboardStatsDTO.RadarStatDTO("Compliance", riskLevel),
            new DashboardStatsDTO.RadarStatDTO("Operational", 30),
            new DashboardStatsDTO.RadarStatDTO("Performance", 50)
        ));

        return stats;
    }
}
