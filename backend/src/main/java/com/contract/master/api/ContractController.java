package com.contract.master.api;

import com.contract.master.domain.AuditLog;
import com.contract.master.dto.ContractDTO;
import com.contract.master.service.ContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/contracts")
public class ContractController {

    @Autowired
    private ContractService contractService;

    @Autowired
    private com.contract.master.service.AuditService auditService;

    @GetMapping
    public GlobalExceptionHandler.ApiResponse<Page<ContractDTO>> list(Pageable pageable) {
        return GlobalExceptionHandler.ApiResponse.success(contractService.searchContracts(pageable));
    }

    @PostMapping
    public GlobalExceptionHandler.ApiResponse<ContractDTO> create(@RequestBody ContractDTO dto) {
        return GlobalExceptionHandler.ApiResponse.success(contractService.createContract(dto));
    }

    @GetMapping("/{id}")
    public GlobalExceptionHandler.ApiResponse<ContractDTO> detail(@PathVariable String id) {
        return GlobalExceptionHandler.ApiResponse.success(contractService.getContractById(id));
    }

    @GetMapping("/{id}/audit")
    public GlobalExceptionHandler.ApiResponse<List<com.contract.master.domain.AuditLog>> audit(@PathVariable String id) {
        return GlobalExceptionHandler.ApiResponse.success(auditService.getAuditLogsByContract(id));
    }

    @PostMapping("/{id}/extensions")
    public GlobalExceptionHandler.ApiResponse<Void> updateExtensions(@PathVariable String id, @RequestBody Map<String, Object> extensions) {
        contractService.saveExtendedData(id, extensions);
        return GlobalExceptionHandler.ApiResponse.success(null);
    }

    @PostMapping("/{id}/verify")
    public GlobalExceptionHandler.ApiResponse<ContractDTO> verifyContract(@PathVariable String id, @RequestBody Map<String, Object> payload) {
        contractService.saveExtendedData(id, payload);
        return GlobalExceptionHandler.ApiResponse.success(contractService.getContractById(id));
    }

    @PostMapping("/batch-archive")
    @com.contract.master.security.HighRiskOperation("Batch Archive Contracts")
    public GlobalExceptionHandler.ApiResponse<Void> batchArchive(@RequestBody List<String> ids) {
        return GlobalExceptionHandler.ApiResponse.success(null);
    }
}
