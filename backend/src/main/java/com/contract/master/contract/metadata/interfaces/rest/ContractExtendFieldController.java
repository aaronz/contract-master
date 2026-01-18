package com.contract.master.contract.metadata.interfaces.rest;

import com.contract.master.api.GlobalExceptionHandler;
import com.contract.master.contract.domain.model.ContractExtendField;
import com.contract.master.contract.domain.repository.ContractExtendFieldRepository;
import com.contract.master.security.TenantContext;
import com.contract.master.shared.domain.model.TenantId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        return GlobalExceptionHandler.ApiResponse.success(HttpStatus.OK, repository.findByTenantId(TenantId.of(TenantContext.getCurrentTenant())));
    }

    @PostMapping
    public GlobalExceptionHandler.ApiResponse<ContractExtendField> create(@Valid @RequestBody ContractExtendField field) {
        String tenantIdStr = TenantContext.getCurrentTenant();
        TenantId tenantId = TenantId.of(tenantIdStr);
        if (repository.findByTenantIdAndFieldCode(tenantId, field.getFieldCode()).isPresent()) {
            throw new RuntimeException("Field code already exists for this tenant: " + field.getFieldCode());
        }
        
        if (field.getFieldId() == null) {
            field.setFieldId(UUID.randomUUID().toString());
        }
        field.setTenantId(tenantId);
        return GlobalExceptionHandler.ApiResponse.success(HttpStatus.OK, repository.save(field));
    }

    @DeleteMapping("/{id}")
    public GlobalExceptionHandler.ApiResponse<Void> delete(@PathVariable Long id) {
        ContractExtendField existing = repository.findById(id).orElse(null);
        if (existing != null) {
            if (!existing.getTenantId().equals(TenantId.of(TenantContext.getCurrentTenant()))) {
                throw new RuntimeException("Unauthorized to delete field for another tenant");
            }
            repository.deleteById(id);
        }
        return GlobalExceptionHandler.ApiResponse.success(HttpStatus.OK, null);
    }
}
