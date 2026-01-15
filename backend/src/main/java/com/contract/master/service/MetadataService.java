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
        fields.add(new FieldMetadataDTO("contractType", "Contract Type", "TEXT", "STANDARD"));
        fields.add(new FieldMetadataDTO("contractStatus", "Status", "TEXT", "STANDARD"));
        fields.add(new FieldMetadataDTO("crmContractId", "CRM ID", "TEXT", "STANDARD"));

        fields.add(new FieldMetadataDTO("partyAName", "Party A Name", "TEXT", "STANDARD"));
        fields.add(new FieldMetadataDTO("partyAContact", "Party A Contact", "TEXT", "STANDARD"));
        fields.add(new FieldMetadataDTO("partyAPhone", "Party A Phone", "TEXT", "STANDARD"));
        fields.add(new FieldMetadataDTO("partyAAddress", "Party A Address", "TEXT", "STANDARD"));

        fields.add(new FieldMetadataDTO("partyBName", "Party B Name", "TEXT", "STANDARD"));
        fields.add(new FieldMetadataDTO("partyBContact", "Party B Contact", "TEXT", "STANDARD"));
        fields.add(new FieldMetadataDTO("partyBPhone", "Party B Phone", "TEXT", "STANDARD"));
        fields.add(new FieldMetadataDTO("partyBAddress", "Party B Address", "TEXT", "STANDARD"));

        fields.add(new FieldMetadataDTO("contractAmount", "Amount", "NUMBER", "STANDARD"));
        fields.add(new FieldMetadataDTO("taxRate", "Tax Rate", "NUMBER", "STANDARD"));
        fields.add(new FieldMetadataDTO("taxAmount", "Tax Amount", "NUMBER", "STANDARD"));
        fields.add(new FieldMetadataDTO("totalAmountWithTax", "Total Amount", "NUMBER", "STANDARD"));
        fields.add(new FieldMetadataDTO("currencyType", "Currency", "TEXT", "STANDARD"));
        fields.add(new FieldMetadataDTO("paymentMethod", "Payment Method", "TEXT", "STANDARD"));
        fields.add(new FieldMetadataDTO("paymentTerm", "Payment Term", "TEXT", "STANDARD"));

        fields.add(new FieldMetadataDTO("invoiceTitle", "Invoice Title", "TEXT", "STANDARD"));
        fields.add(new FieldMetadataDTO("taxpayerId", "Taxpayer ID", "TEXT", "STANDARD"));
        fields.add(new FieldMetadataDTO("invoiceType", "Invoice Type", "TEXT", "STANDARD"));

        fields.add(new FieldMetadataDTO("signDate", "Sign Date", "DATE", "STANDARD"));
        fields.add(new FieldMetadataDTO("effectiveDate", "Effective Date", "DATE", "STANDARD"));
        fields.add(new FieldMetadataDTO("expireDate", "Expire Date", "DATE", "STANDARD"));

        fields.add(new FieldMetadataDTO("performanceLocation", "Performance Location", "TEXT", "STANDARD"));
        fields.add(new FieldMetadataDTO("performanceMethod", "Performance Method", "TEXT", "STANDARD"));
        fields.add(new FieldMetadataDTO("performanceStartDate", "Performance Start", "DATE", "STANDARD"));
        fields.add(new FieldMetadataDTO("performanceEndDate", "Performance End", "DATE", "STANDARD"));
        fields.add(new FieldMetadataDTO("qualityStandard", "Quality Standard", "TEXT", "STANDARD"));

        fields.add(new FieldMetadataDTO("legalReviewFlag", "Legal Review", "BOOLEAN", "STANDARD"));
        fields.add(new FieldMetadataDTO("legalReviewOpinion", "Legal Opinion", "TEXT", "STANDARD"));
        fields.add(new FieldMetadataDTO("disputeResolution", "Dispute Resolution", "TEXT", "STANDARD"));
        fields.add(new FieldMetadataDTO("governingLaw", "Governing Law", "TEXT", "STANDARD"));

        fields.add(new FieldMetadataDTO("subjectType", "Subject Type", "TEXT", "STANDARD"));
        fields.add(new FieldMetadataDTO("subjectDesc", "Subject Description", "TEXT", "STANDARD"));
        fields.add(new FieldMetadataDTO("subjectQuantity", "Quantity", "NUMBER", "STANDARD"));
        fields.add(new FieldMetadataDTO("unitPrice", "Unit Price", "NUMBER", "STANDARD"));

        fields.add(new FieldMetadataDTO("remark", "Remark", "TEXT", "STANDARD"));
        fields.add(new FieldMetadataDTO("createUser", "Creator", "TEXT", "STANDARD"));
        fields.add(new FieldMetadataDTO("createTime", "Create Time", "DATE", "STANDARD"));

        List<ContractExtendField> extendFields = extendFieldRepository.findByTenantId(tenantId);
        fields.addAll(extendFields.stream()
                .map(f -> new FieldMetadataDTO(f.getFieldCode(), f.getFieldName(), f.getFieldType(), "EXTEND"))
                .collect(Collectors.toList()));

        return fields;
    }
}
