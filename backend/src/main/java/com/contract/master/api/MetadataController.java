package com.contract.master.api;

import com.contract.master.domain.ContractExtendField;
import com.contract.master.domain.ContractExtendFieldRepository;
import com.contract.master.dto.FieldMetadataDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/metadata")
public class MetadataController {

    @Autowired
    private ContractExtendFieldRepository extendFieldRepository;

    @GetMapping("/contract-fields")
    public List<FieldMetadataDTO> getContractFields() {
        List<FieldMetadataDTO> fields = new ArrayList<>();
        
        fields.add(new FieldMetadataDTO("contractNo", "Contract Number", "TEXT", "STANDARD"));
        fields.add(new FieldMetadataDTO("contractName", "Contract Name", "TEXT", "STANDARD"));
        fields.add(new FieldMetadataDTO("partyAName", "Party A Name", "TEXT", "STANDARD"));
        fields.add(new FieldMetadataDTO("partyBName", "Party B Name", "TEXT", "STANDARD"));
        fields.add(new FieldMetadataDTO("amount", "Amount", "NUMBER", "STANDARD"));
        fields.add(new FieldMetadataDTO("status", "Status", "TEXT", "STANDARD"));
        fields.add(new FieldMetadataDTO("effectiveDate", "Effective Date", "DATE", "STANDARD"));
        fields.add(new FieldMetadataDTO("expireDate", "Expire Date", "DATE", "STANDARD"));

        List<ContractExtendField> extendFields = extendFieldRepository.findAll();
        fields.addAll(extendFields.stream()
                .map(f -> new FieldMetadataDTO(f.getFieldCode(), f.getFieldName(), f.getFieldType(), "EXTEND"))
                .collect(Collectors.toList()));

        return fields;
    }
}
