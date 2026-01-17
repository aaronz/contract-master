package com.contract.master.contract.interfaces.rest;

import com.contract.master.api.GlobalExceptionHandler;
import com.contract.master.contract.application.ContractApplicationService;
import com.contract.master.contract.domain.model.Contract;
import com.contract.master.contract.domain.repository.ContractRepository;
import com.contract.master.dto.ContractDTO;
import com.contract.master.security.TenantContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/contracts")
public class ContractCommandController {
    private final ContractApplicationService applicationService;
    private final ContractRepository contractRepository;

    public ContractCommandController(ContractApplicationService applicationService, ContractRepository contractRepository) {
        this.applicationService = applicationService;
        this.contractRepository = contractRepository;
    }

    @GetMapping
    public GlobalExceptionHandler.ApiResponse<Page<Contract>> list(Pageable pageable) {
        return GlobalExceptionHandler.ApiResponse.success(HttpStatus.OK, 
            contractRepository.findByTenantId(TenantContext.getCurrentTenant(), pageable));
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
}
