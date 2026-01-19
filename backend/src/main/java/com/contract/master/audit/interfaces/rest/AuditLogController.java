package com.contract.master.audit.interfaces.rest;

import com.contract.master.audit.application.AuditService;
import com.contract.master.audit.domain.model.AuditLog;
import com.contract.master.api.GlobalExceptionHandler;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/audit")
public class AuditLogController {

    private final AuditService auditService;

    public AuditLogController(AuditService auditService) {
        this.auditService = auditService;
    }

    @GetMapping("/logs")
    public GlobalExceptionHandler.ApiResponse<List<AuditLog>> getAllLogs() {
        return GlobalExceptionHandler.ApiResponse.success(HttpStatus.OK, auditService.getAllAuditLogs());
    }
}
