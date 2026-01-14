package com.contract.master.controller;

import com.contract.master.dto.ContractDTO;
import com.contract.master.service.ContractService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public List<ContractDTO> list() {
        return contractService.getAllContracts();
    }

    @GetMapping("/{id}")
    public ContractDTO detail(@PathVariable String id) {
        return contractService.getContractById(id);
    }

    @GetMapping("/{id}/audit")
    public List<com.contract.master.entity.AuditLog> audit(@PathVariable String id) {
        return auditService.getAuditLogsByContract(id);
    }

    @PostMapping("/{id}/extensions")
    public void updateExtensions(@PathVariable String id, @RequestBody Map<String, Object> extensions) {
        contractService.saveExtendedData(id, extensions);
    }

    @PostMapping("/batch-archive")
    @com.contract.master.security.HighRiskOperation("Batch Archive Contracts")
    public void batchArchive(@RequestBody List<String> ids) {
    }
}
