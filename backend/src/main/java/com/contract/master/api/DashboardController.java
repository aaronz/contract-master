package com.contract.master.api;

import com.contract.master.dto.DashboardStatsDTO;
import com.contract.master.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/stats")
    public GlobalExceptionHandler.ApiResponse<DashboardStatsDTO> getStats() {
        return GlobalExceptionHandler.ApiResponse.success(dashboardService.getStats());
    }
}
