package com.contract.master.contract.metadata.interfaces.rest;

import com.contract.master.api.GlobalExceptionHandler;
import com.contract.master.contract.metadata.dto.FieldMetadataDTO;
import com.contract.master.contract.metadata.application.MetadataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/metadata")
public class MetadataController {

    @Autowired
    private MetadataService metadataService;

    @GetMapping("/contract-fields")
    public GlobalExceptionHandler.ApiResponse<List<FieldMetadataDTO>> getContractFields() {
        return GlobalExceptionHandler.ApiResponse.success(HttpStatus.OK, metadataService.getContractFields());
    }
}