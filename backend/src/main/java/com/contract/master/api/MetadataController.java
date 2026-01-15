package com.contract.master.api;

import com.contract.master.dto.FieldMetadataDTO;
import com.contract.master.service.MetadataService;
import org.springframework.beans.factory.annotation.Autowired;
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
        return GlobalExceptionHandler.ApiResponse.success(metadataService.getContractFields());
    }
}
