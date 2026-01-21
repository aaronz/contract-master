package com.contract.master.contract.application;

import com.contract.master.contract.domain.model.ContractExtendField;
import com.contract.master.contract.domain.repository.ContractExtendFieldRepository;
import com.contract.master.contract.dto.ContractDTO;
import com.contract.master.contract.metadata.application.MetadataService;
import com.contract.master.contract.metadata.dto.FieldMetadataDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ExportService {

    @Autowired
    private ContractService contractService;

    @Autowired
    private MetadataService metadataService;

    public void exportToCsv(java.io.PrintWriter writer) {
        List<ContractDTO> contracts = contractService.getAllContracts();
        List<FieldMetadataDTO> fields = metadataService.getContractFields().stream()
                .filter(f -> Boolean.TRUE.equals(f.getIsVisible()))
                .sorted(java.util.Comparator.comparing(FieldMetadataDTO::getDisplayOrder))
                .collect(Collectors.toList());

        String header = fields.stream()
                .map(FieldMetadataDTO::getFieldName)
                .collect(Collectors.joining(","));
        writer.println(header);

        for (ContractDTO c : contracts) {
            org.springframework.beans.BeanWrapper src = new org.springframework.beans.BeanWrapperImpl(c);
            String row = fields.stream()
                    .map(f -> {
                        Object val = null;
                        if ("STANDARD".equals(f.getSource())) {
                            String prop = snakeToCamelCase(f.getFieldCode());
                            if (src.isReadableProperty(prop)) {
                                val = src.getPropertyValue(prop);
                            }
                        } else {
                            val = c.getExtendedFields() != null ? c.getExtendedFields().get(f.getFieldCode()) : "";
                        }
                        return val != null ? val.toString().replace(",", " ") : "";
                    })
                    .collect(Collectors.joining(","));
            writer.println(row);
        }
    }

    private String snakeToCamelCase(String snakeCase) {
        if (snakeCase == null || snakeCase.isEmpty()) return snakeCase;
        StringBuilder result = new StringBuilder();
        boolean nextUpper = false;
        for (char c : snakeCase.toCharArray()) {
            if (c == '_') {
                nextUpper = true;
            } else {
                if (nextUpper) {
                    result.append(Character.toUpperCase(c));
                    nextUpper = false;
                } else {
                    result.append(c);
                }
            }
        }
        return result.toString();
    }

    public byte[] exportToPdf(String id) {
        return new byte[0];
    }
}
