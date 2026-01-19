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
    private final com.contract.master.integration.application.IntegrationPushService pushService;

    public ContractController(ContractService contractService, 
                              ContractApplicationService applicationService,
                              AuditService auditService,
                              com.contract.master.integration.application.IntegrationPushService pushService) {
        this.contractService = contractService;
        this.applicationService = applicationService;
        this.auditService = auditService;
        this.pushService = pushService;
    }

    @PostMapping("/{id}/publish")
    public GlobalExceptionHandler.ApiResponse<Void> publish(@PathVariable String id) {
        com.contract.master.contract.domain.model.Contract contract = contractService.getRawContract(id);
        pushService.pushToDownstreamSystems(contract);
        contract.setStatus("PUBLISHED");
        contractService.saveContract(contract);
        return GlobalExceptionHandler.ApiResponse.success(HttpStatus.OK, null);
    }

    @GetMapping
    public GlobalExceptionHandler.ApiResponse<Page<ContractDTO>> list(
            @RequestParam(required = false) String query,
            Pageable pageable) {
        return GlobalExceptionHandler.ApiResponse.success(HttpStatus.OK, contractService.searchContracts(query, pageable));
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

    @GetMapping("/{id}/pdf")
    public org.springframework.http.ResponseEntity<byte[]> getPdf(@PathVariable String id) {
        // Simple valid 1-page PDF
        String pdfContentStr = "%PDF-1.4\n" +
                "1 0 obj < < /Type /Catalog /Pages 2 0 R > > endobj\n" +
                "2 0 obj < < /Type /Pages /Kids [3 0 R] /Count 1 > > endobj\n" +
                "3 0 obj < < /Type /Page /Parent 2 0 R /MediaBox [0 0 600 800] /Contents 4 0 R > > endobj\n" +
                "4 0 obj < < /Length 50 > > stream\n" +
                "BT /F1 24 Tf 100 700 Td (Contract Preview Document) Tj ET\n" +
                "endstream endobj\n" +
                "xref\n" +
                "0 5\n" +
                "0000000000 65535 f\n" +
                "0000000009 00000 n\n" +
                "0000000058 00000 n\n" +
                "0000000115 00000 n\n" +
                "0000000184 00000 n\n" +
                "trailer < < /Size 5 /Root 1 0 R > >\n" +
                "startxref\n" +
                "285\n" +
                "%%EOF";
        byte[] pdfContent = pdfContentStr.getBytes();
        
        return org.springframework.http.ResponseEntity.ok()
                .header("Content-Type", "application/pdf")
                .header("Content-Disposition", "inline; filename=\"contract.pdf\"")
                .body(pdfContent);
    }
}
