package com.contract.master.integration.interfaces.rest;

import com.contract.master.api.GlobalExceptionHandler;
import com.contract.master.integration.domain.model.FieldMapping;
import com.contract.master.integration.domain.repository.FieldMappingRepository;
import com.contract.master.security.TenantContext;
import com.contract.master.shared.domain.model.TenantId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*; // Import all annotations from rest

import java.util.List;

@RestController
@RequestMapping("/api/v1/settings/mapping")
public class FieldMappingController {

    @Autowired
    private FieldMappingRepository repository;

    @GetMapping
    public GlobalExceptionHandler.ApiResponse<List<FieldMapping>> list() {
        return GlobalExceptionHandler.ApiResponse.success(HttpStatus.OK, repository.findAll());
    }

    @PostMapping
    public GlobalExceptionHandler.ApiResponse<FieldMapping> save(@RequestBody FieldMapping mapping) {
        return GlobalExceptionHandler.ApiResponse.success(HttpStatus.OK, repository.save(mapping));
    }

    @DeleteMapping("/{id}")
    public GlobalExceptionHandler.ApiResponse<Void> delete(@PathVariable Long id) {
        repository.deleteById(id);
        return GlobalExceptionHandler.ApiResponse.success(HttpStatus.OK, null);
    }
}
