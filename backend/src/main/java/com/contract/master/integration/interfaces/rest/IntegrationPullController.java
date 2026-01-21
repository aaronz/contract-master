package com.contract.master.integration.interfaces.rest;

import com.contract.master.api.GlobalExceptionHandler;
import com.contract.master.integration.application.IntegrationPullService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/integration/contracts")
public class IntegrationPullController {

    @Autowired
    private IntegrationPullService pullService;

    @GetMapping
    public GlobalExceptionHandler.ApiResponse<List<Map<String, Object>>> pull(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime updatedSince,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            HttpServletRequest request) {
        
        String systemId = (String) request.getAttribute("targetSystemId");
        Pageable pageable = PageRequest.of(page, size);
        
        List<Map<String, Object>> result = pullService.pullContracts(systemId, updatedSince, pageable);
        pullService.logPullEvent(systemId, result.size());
        
        return GlobalExceptionHandler.ApiResponse.success(HttpStatus.OK, result);
    }
}
