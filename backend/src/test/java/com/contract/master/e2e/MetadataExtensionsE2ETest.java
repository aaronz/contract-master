package com.contract.master.e2e;

import com.contract.master.contract.domain.model.Contract;
import com.contract.master.contract.domain.model.ContractId;
import com.contract.master.contract.domain.model.ContractNo;
import com.contract.master.contract.domain.repository.ContractRepository;
import com.contract.master.security.TenantContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.test.context.support.WithMockUser;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
public class MetadataExtensionsE2ETest {

    @Autowired
    private ContractRepository contractRepository;

    @Autowired
    private com.contract.master.contract.application.ContractService contractService;

    @Autowired
    private com.contract.master.contract.application.ExtendFieldService extendFieldService;

    @Autowired
    private com.contract.master.contract.domain.repository.ContractExtendDataRepository extendDataRepository;

    @Autowired
    private com.contract.master.contract.application.ExportService exportService;

    @Autowired
    private com.contract.master.contract.metadata.domain.repository.FieldConfigRepository fieldConfigRepository;

    @AfterEach
    void tearDown() {
        TenantContext.clear();
    }

    @Test
    void shouldPersistCustomFieldValueAndAudit() {
        String tenantId = "tenant-1";
        TenantContext.setCurrentTenant(tenantId);
        
        com.contract.master.contract.domain.model.ContractExtendField field = new com.contract.master.contract.domain.model.ContractExtendField();
        field.setFieldName("External ID");
        field.setFieldCode("external_id");
        field.setFieldType("TEXT");
        field.setIsRequired(true);
        extendFieldService.createField(field);

        com.contract.master.contract.dto.ContractDTO dto = new com.contract.master.contract.dto.ContractDTO();
        dto.setContractNo("E2E-METADATA-001");
        dto.setContractName("Metadata Test Contract");
        dto.setExtendedFields(Map.of("external_id", "EXT-999"));
        
        com.contract.master.contract.dto.ContractDTO saved = contractService.createContract(dto);

        assertThat(saved.getExtendedFields().get("external_id")).isEqualTo("EXT-999");
        
        var dbData = extendDataRepository.findByContractId(saved.getContractId());
        assertThat(dbData).isNotEmpty();
        assertThat(dbData.get(0).getFieldValue()).isEqualTo("EXT-999");
    }

    @Test
    void shouldValidateCustomFieldTypes() {
        String tenantId = "tenant-val";
        TenantContext.setCurrentTenant(tenantId);
        
        com.contract.master.contract.domain.model.ContractExtendField field = new com.contract.master.contract.domain.model.ContractExtendField();
        field.setFieldName("Amount Override");
        field.setFieldCode("amount_override");
        field.setFieldType("NUMBER");
        extendFieldService.createField(field);

        com.contract.master.contract.dto.ContractDTO dto = new com.contract.master.contract.dto.ContractDTO();
        dto.setContractNo("VAL-001");
        dto.setExtendedFields(Map.of("amount_override", "NOT_A_NUMBER"));
        
        assertThrows(IllegalArgumentException.class, () -> {
            contractService.createContract(dto);
        });
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void shouldHideFieldsBasedOnRBAC() {
        String tenantId = "tenant-rbac";
        TenantContext.setCurrentTenant(tenantId);
        
        com.contract.master.contract.metadata.domain.model.FieldConfig config = new com.contract.master.contract.metadata.domain.model.FieldConfig();
        config.setFieldCode("contract_amount");
        config.setRequiredRole("ROLE_ADMIN");
        config.setIsVisible(true);
        config.setConfigType("CONTRACT");
        fieldConfigRepository.save(config);
        contractService.clearFieldConfigCache();

        com.contract.master.contract.domain.model.Contract contract = new com.contract.master.contract.domain.model.Contract();
        contract.setContractId(ContractId.of(UUID.randomUUID().toString()));
        contract.setContractNo(new ContractNo("RBAC-001"));
        contract.setAmount(new com.contract.master.contract.domain.model.ContractAmount(new java.math.BigDecimal("5000"), null, null, null, "USD"));
        contractRepository.save(contract);

        com.contract.master.contract.dto.ContractDTO result = contractService.getContractById(contract.getContractId().value().toString());

        assertThat(result.getContractAmount()).isNull();
    }

    @Test
    void shouldIncludeExtendedFieldsInCsvExport() {
        String tenantId = "tenant-export";
        TenantContext.setCurrentTenant(tenantId);
        
        com.contract.master.contract.domain.model.ContractExtendField field = new com.contract.master.contract.domain.model.ContractExtendField();
        field.setFieldName("Export Field");
        field.setFieldCode("export_field");
        field.setFieldType("TEXT");
        extendFieldService.createField(field);

        com.contract.master.contract.dto.ContractDTO dto = new com.contract.master.contract.dto.ContractDTO();
        dto.setContractNo("EXPORT-001");
        dto.setContractName("Export Test Contract");
        dto.setExtendedFields(Map.of("export_field", "ExportValue123"));
        contractService.createContract(dto);

        StringWriter sw = new StringWriter();
        exportService.exportToCsv(new PrintWriter(sw));
        String csv = sw.toString();

        assertThat(csv).contains("Export Field");
        assertThat(csv).contains("ExportValue123");
    }
}
