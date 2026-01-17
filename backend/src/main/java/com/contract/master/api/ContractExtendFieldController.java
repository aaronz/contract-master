package com.contract.master.api;

import com.contract.master.domain.ContractExtendField;
import com.contract.master.domain.ContractExtendFieldRepository;
import com.contract.master.security.TenantContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/settings/extend-fields")
public class ContractExtendFieldController {

    @Autowired
    private ContractExtendFieldRepository repository;

    @GetMapping
    public GlobalExceptionHandler.ApiResponse<List<ContractExtendField>> list() {
        return GlobalExceptionHandler.ApiResponse.success(repository.findByTenantId(TenantContext.getCurrentTenant()));
    }

    @PostMapping
    public GlobalExceptionHandler.ApiResponse<ContractExtendField> create(@Valid @RequestBody ContractExtendField field) {
        String tenantId = TenantContext.getCurrentTenant();
        if (repository.findByTenantIdAndFieldCode(tenantId, field.getFieldCode()).isPresent()) {
            throw new RuntimeException("Field code already exists for this tenant: " + field.getFieldCode());
        }
        
        if (field.getFieldId() == null) {
            field.setFieldId(UUID.randomUUID().toString());
        }
        field.setTenantId(tenantId);
        return GlobalExceptionHandler.ApiResponse.success(repository.save(field));
    }

    @DeleteMapping("/{id}")
    public GlobalExceptionHandler.ApiResponse<Void> delete(@PathVariable String id) {
        ContractExtendField existing = repository.findById(id).orElse(null);
        if (existing != null) {
            if (!existing.getTenantId().equals(TenantContext.getCurrentTenant())) {
                throw new RuntimeException("Unauthorized to delete field for another tenant");
            }
            repository.deleteById(id);
        }
        return GlobalExceptionHandler.ApiResponse.success(null);
    }
}
