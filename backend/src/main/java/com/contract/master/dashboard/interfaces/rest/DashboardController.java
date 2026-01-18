package com.contract.master.dashboard.interfaces.rest;

import com.contract.master.dashboard.dto.DashboardStatsDTO;
import com.contract.master.dashboard.application.DashboardService;
import com.contract.master.api.GlobalExceptionHandler;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/stats")
    public GlobalExceptionHandler.ApiResponse<DashboardStatsDTO> getStats() {
        return GlobalExceptionHandler.ApiResponse.success(HttpStatus.OK, dashboardService.getStats());
    }
}
