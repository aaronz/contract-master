package com.contract.master.contract.metadata.interfaces.rest;

import com.contract.master.api.GlobalExceptionHandler;
import com.contract.master.contract.domain.model.ContractExtendField;
import com.contract.master.contract.application.ExtendFieldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/settings/extend-fields")
public class ContractExtendFieldController {

    @Autowired
    private ExtendFieldService service;

    @GetMapping
    public GlobalExceptionHandler.ApiResponse<List<ContractExtendField>> list() {
        return GlobalExceptionHandler.ApiResponse.success(HttpStatus.OK, service.getAllFields());
    }

    @PostMapping
    public GlobalExceptionHandler.ApiResponse<ContractExtendField> create(@Valid @RequestBody ContractExtendField field) {
        return GlobalExceptionHandler.ApiResponse.success(HttpStatus.OK, service.createField(field));
    }

    @DeleteMapping("/{id}")
    public GlobalExceptionHandler.ApiResponse<Void> delete(@PathVariable Long id) {
        service.deleteField(id);
        return GlobalExceptionHandler.ApiResponse.success(HttpStatus.OK, null);
    }
}
