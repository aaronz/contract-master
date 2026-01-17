package com.contract.master.api;

import com.contract.master.dto.DashboardStatsDTO;
import com.contract.master.dashboard.application.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*; // Import all annotations from rest

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/stats")
    public GlobalExceptionHandler.ApiResponse<DashboardStatsDTO> getStats() {
        return GlobalExceptionHandler.ApiResponse.success(HttpStatus.OK, dashboardService.getStats());
    }
}
