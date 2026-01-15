package com.contract.master.api;

import com.contract.master.domain.DownstreamSystem;
import com.contract.master.domain.DownstreamSystemRepository;
import com.contract.master.security.TenantContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/settings/downstream")
public class DownstreamSystemController {

    @Autowired
    private DownstreamSystemRepository repository;

    @GetMapping
    public GlobalExceptionHandler.ApiResponse<List<DownstreamSystem>> list() {
        return GlobalExceptionHandler.ApiResponse.success(repository.findByTenantId(TenantContext.getCurrentTenant()));
    }

    @PostMapping
    public GlobalExceptionHandler.ApiResponse<DownstreamSystem> save(@RequestBody DownstreamSystem system) {
        if (system.getSystemId() == null) {
            system.setSystemId(java.util.UUID.randomUUID().toString());
        }
        system.setTenantId(TenantContext.getCurrentTenant());
        return GlobalExceptionHandler.ApiResponse.success(repository.save(system));
    }
}
