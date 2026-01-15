package com.contract.master.api;

import com.contract.master.domain.FieldMapping;
import com.contract.master.domain.FieldMappingRepository;
import com.contract.master.security.TenantContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/settings/mapping")
public class FieldMappingController {

    @Autowired
    private FieldMappingRepository repository;

    @GetMapping
    public GlobalExceptionHandler.ApiResponse<List<FieldMapping>> list() {
        return GlobalExceptionHandler.ApiResponse.success(repository.findByTenantId(TenantContext.getCurrentTenant()));
    }

    @PostMapping
    public GlobalExceptionHandler.ApiResponse<FieldMapping> save(@RequestBody FieldMapping mapping) {
        mapping.setTenantId(TenantContext.getCurrentTenant());
        return GlobalExceptionHandler.ApiResponse.success(repository.save(mapping));
    }

    @DeleteMapping("/{id}")
    public GlobalExceptionHandler.ApiResponse<Void> delete(@PathVariable Long id) {
        repository.deleteById(id);
        return GlobalExceptionHandler.ApiResponse.success(null);
    }
}
