package com.contract.master.service;

import com.contract.master.domain.ContractExtendField;
import com.contract.master.domain.ContractExtendFieldRepository;
import com.contract.master.dto.FieldMetadataDTO;
import com.contract.master.security.TenantContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MetadataService {

    @Autowired
    private ContractExtendFieldRepository extendFieldRepository;

    public List<FieldMetadataDTO> getContractFields() {
        String tenantId = TenantContext.getCurrentTenant();
        List<FieldMetadataDTO> fields = new ArrayList<>();
        
        fields.add(new FieldMetadataDTO("contractNo", "Contract Number", "TEXT", "STANDARD"));
        fields.add(new FieldMetadataDTO("contractName", "Contract Name", "TEXT", "STANDARD"));
        fields.add(new FieldMetadataDTO("partyAName", "Party A Name", "TEXT", "STANDARD"));
        fields.add(new FieldMetadataDTO("partyBName", "Party B Name", "TEXT", "STANDARD"));
        fields.add(new FieldMetadataDTO("amount", "Amount", "NUMBER", "STANDARD"));
        fields.add(new FieldMetadataDTO("status", "Status", "TEXT", "STANDARD"));
        fields.add(new FieldMetadataDTO("effectiveDate", "Effective Date", "DATE", "STANDARD"));
        fields.add(new FieldMetadataDTO("expireDate", "Expire Date", "DATE", "STANDARD"));
        fields.add(new FieldMetadataDTO("contractType", "Contract Type", "TEXT", "STANDARD"));
        fields.add(new FieldMetadataDTO("partyAId", "Party A ID", "TEXT", "STANDARD"));
        fields.add(new FieldMetadataDTO("signDate", "Sign Date", "DATE", "STANDARD"));
        fields.add(new FieldMetadataDTO("taxRate", "Tax Rate", "NUMBER", "STANDARD"));
        fields.add(new FieldMetadataDTO("currencyType", "Currency", "TEXT", "STANDARD"));
        fields.add(new FieldMetadataDTO("paymentMethod", "Payment Method", "TEXT", "STANDARD"));
        fields.add(new FieldMetadataDTO("invoiceTitle", "Invoice Title", "TEXT", "STANDARD"));
        fields.add(new FieldMetadataDTO("taxpayerId", "Taxpayer ID", "TEXT", "STANDARD"));
        fields.add(new FieldMetadataDTO("performanceLocation", "Performance Location", "TEXT", "STANDARD"));
        fields.add(new FieldMetadataDTO("governingLaw", "Governing Law", "TEXT", "STANDARD"));
        fields.add(new FieldMetadataDTO("remark", "Remark", "TEXT", "STANDARD"));

        List<ContractExtendField> extendFields = extendFieldRepository.findByTenantId(tenantId);
        fields.addAll(extendFields.stream()
                .map(f -> new FieldMetadataDTO(f.getFieldCode(), f.getFieldName(), f.getFieldType(), "EXTEND"))
                .collect(Collectors.toList()));

        return fields;
    }

}
