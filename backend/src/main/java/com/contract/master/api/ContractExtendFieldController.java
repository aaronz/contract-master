package com.contract.master.api;

import com.contract.master.domain.ContractExtendField;
import com.contract.master.domain.ContractExtendFieldRepository;
import com.contract.master.security.TenantContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

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
    public GlobalExceptionHandler.ApiResponse<ContractExtendField> create(@RequestBody ContractExtendField field) {
        if (field.getFieldId() == null) {
            field.setFieldId(UUID.randomUUID().toString());
        }
        field.setTenantId(TenantContext.getCurrentTenant());
        return GlobalExceptionHandler.ApiResponse.success(repository.save(field));
    }

    @DeleteMapping("/{id}")
    public GlobalExceptionHandler.ApiResponse<Void> delete(@PathVariable String id) {
        repository.deleteById(id);
        return GlobalExceptionHandler.ApiResponse.success(null);
    }
}
