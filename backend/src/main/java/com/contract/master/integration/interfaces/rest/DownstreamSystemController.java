package com.contract.master.integration.interfaces.rest;

import com.contract.master.api.GlobalExceptionHandler;
import com.contract.master.integration.domain.model.DownstreamSystem;
import com.contract.master.integration.domain.repository.DownstreamSystemRepository;
import com.contract.master.security.TenantContext;
import com.contract.master.shared.domain.model.TenantId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/settings/downstream")
public class DownstreamSystemController {

    @Autowired
    private DownstreamSystemRepository repository;

    @GetMapping
    public GlobalExceptionHandler.ApiResponse<List<DownstreamSystem>> list() {
        return GlobalExceptionHandler.ApiResponse.success(HttpStatus.OK, repository.findAll());
    }

    @PostMapping
    public GlobalExceptionHandler.ApiResponse<DownstreamSystem> create(@RequestBody DownstreamSystem system) {
        if (system.getSystemId() == null) {
            system.setSystemId(UUID.randomUUID().toString());
        }
        if (system.getAccessKey() == null) {
            system.setAccessKey("AK_" + UUID.randomUUID().toString().replace("-", "").substring(0, 16));
        }
        return GlobalExceptionHandler.ApiResponse.success(HttpStatus.OK, repository.save(system));
    }

    @PutMapping("/{id}")
    public GlobalExceptionHandler.ApiResponse<DownstreamSystem> update(@PathVariable String id, @RequestBody DownstreamSystem system) {
        system.setSystemId(id);
        return GlobalExceptionHandler.ApiResponse.success(HttpStatus.OK, repository.save(system));
    }
}
