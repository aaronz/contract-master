package com.contract.master.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class DashboardStatsDTO {
    private Long totalContracts;
    private Long pendingApprovals;
    private BigDecimal activeValue;
    private Long riskAlerts;
    private List<DailyCountDTO> volumeTrend;
    private List<RadarStatDTO> riskRadar;

    public DashboardStatsDTO() {}

    public Long getTotalContracts() { return totalContracts; }
    public void setTotalContracts(Long totalContracts) { this.totalContracts = totalContracts; }
    public Long getPendingApprovals() { return pendingApprovals; }
    public void setPendingApprovals(Long pendingApprovals) { this.pendingApprovals = pendingApprovals; }
    public BigDecimal getActiveValue() { return activeValue; }
    public void setActiveValue(BigDecimal activeValue) { this.activeValue = activeValue; }
    public Long getRiskAlerts() { return riskAlerts; }
    public void setRiskAlerts(Long riskAlerts) { this.riskAlerts = riskAlerts; }
    public List<DailyCountDTO> getVolumeTrend() { return volumeTrend; }
    public void setVolumeTrend(List<DailyCountDTO> volumeTrend) { this.volumeTrend = volumeTrend; }
    public List<RadarStatDTO> getRiskRadar() { return riskRadar; }
    public void setRiskRadar(List<RadarStatDTO> riskRadar) { this.riskRadar = riskRadar; }

    public static class DailyCountDTO {
        private String date;
        private Long count;

        public DailyCountDTO(String date, Long count) {
            this.date = date;
            this.count = count;
        }
        public String getDate() { return date; }
        public Long getCount() { return count; }
    }

    public static class RadarStatDTO {
        private String name;
        private Integer value;

        public RadarStatDTO(String name, Integer value) {
            this.name = name;
            this.value = value;
        }
        public String getName() { return name; }
        public Integer getValue() { return value; }
    }
}
