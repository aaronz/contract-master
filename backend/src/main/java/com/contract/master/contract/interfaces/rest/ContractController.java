package com.contract.master.contract.interfaces.rest;

import com.contract.master.audit.domain.model.AuditLog;
import com.contract.master.contract.dto.ContractDTO;
import com.contract.master.contract.application.ContractService;
import com.contract.master.contract.application.ContractApplicationService;
import com.contract.master.api.GlobalExceptionHandler;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import com.contract.master.audit.application.AuditService;

@RestController
@RequestMapping("/api/contracts")
public class ContractController {

    private final ContractService contractService;
    private final ContractApplicationService applicationService;
    private final AuditService auditService;

    public ContractController(ContractService contractService, 
                              ContractApplicationService applicationService,
                              AuditService auditService) {
        this.contractService = contractService;
        this.applicationService = applicationService;
        this.auditService = auditService;
    }

    @GetMapping
    public GlobalExceptionHandler.ApiResponse<Page<ContractDTO>> list(Pageable pageable) {
        return GlobalExceptionHandler.ApiResponse.success(HttpStatus.OK, contractService.searchContracts(pageable));
    }

    @PostMapping
    public GlobalExceptionHandler.ApiResponse<ContractDTO> create(@RequestBody ContractDTO dto) {
        return GlobalExceptionHandler.ApiResponse.success(HttpStatus.OK, contractService.createContract(dto));
    }

    @GetMapping("/{id}")
    public GlobalExceptionHandler.ApiResponse<ContractDTO> detail(@PathVariable String id) {
        return GlobalExceptionHandler.ApiResponse.success(HttpStatus.OK, contractService.getContractById(id));
    }

    @PutMapping("/{id}")
    public GlobalExceptionHandler.ApiResponse<Void> update(@PathVariable String id, @RequestBody ContractDTO dto) {
        contractService.updateContractFromDTO(id, dto);
        return GlobalExceptionHandler.ApiResponse.success(HttpStatus.OK, null);
    }

    @GetMapping("/{id}/audit")
    public GlobalExceptionHandler.ApiResponse<List<AuditLog>> audit(@PathVariable String id) {
        return GlobalExceptionHandler.ApiResponse.success(HttpStatus.OK, auditService.getAuditLogsByContract(id));
    }

    @PostMapping("/{id}/extensions")
    public GlobalExceptionHandler.ApiResponse<Void> updateExtensions(@PathVariable String id, @RequestBody Map<String, Object> extensions) {
        contractService.saveExtendedData(id, extensions);
        return GlobalExceptionHandler.ApiResponse.success(HttpStatus.OK, null);
    }

    @PostMapping("/{id}/verify")
    public GlobalExceptionHandler.ApiResponse<Void> verify(@PathVariable String id, Authentication auth) {
        applicationService.verifyContract(id, auth.getName());
        return GlobalExceptionHandler.ApiResponse.success(HttpStatus.OK, null);
    }

    @PostMapping("/{id}/archive")
    public GlobalExceptionHandler.ApiResponse<Void> archive(@PathVariable String id, Authentication auth) {
        applicationService.archiveContract(id, auth.getName());
        return GlobalExceptionHandler.ApiResponse.success(HttpStatus.OK, null);
    }

    @PostMapping("/batch-archive")
    @com.contract.master.security.HighRiskOperation("Batch Archive Contracts")
    public GlobalExceptionHandler.ApiResponse<Void> batchArchive(@RequestBody List<String> ids) {
        return GlobalExceptionHandler.ApiResponse.success(HttpStatus.OK, null);
    }
}
