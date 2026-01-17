package com.contract.master.contract.metadata.application;

import com.contract.master.contract.domain.model.ContractExtendField;
import com.contract.master.contract.domain.repository.ContractExtendFieldRepository;
import com.contract.master.contract.metadata.domain.model.FieldConfig;
import com.contract.master.contract.metadata.domain.repository.FieldConfigRepository;
import com.contract.master.dto.FieldMetadataDTO;
import com.contract.master.security.TenantContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.AfterEach; // Import AfterEach
import com.contract.master.contract.application.ContractService;

@SpringBootTest
@Transactional
public class FieldMetadataIntegrationTest {

    @Autowired
    private MetadataService metadataService;

    @Autowired
    private FieldConfigRepository fieldConfigRepository;

    @Autowired
    private ContractExtendFieldRepository extendFieldRepository;

    @Autowired
    private ContractService contractService; // Inject ContractService

    @BeforeEach
    void setUp() {
        TenantContext.setCurrentTenant("test-tenant");
        metadataService.clearFieldConfigCache(); // Clear MetadataService cache
    }

    @AfterEach
    void tearDown() {
        // This is an integration test, so no need to clear the contractService cache here.
        // It's the responsibility of ContractServiceTest or a broader integration test
        // to manage its cache.
    }


    @Test
    void testUnifiedMetadataWithConfigs() {
        FieldConfig config = new FieldConfig();
        config.setFieldCode("contract_no"); // Corrected to snake_case
        config.setFieldAlias("Custom Contract No");
        config.setIsVisible(false);
        config.setTenantId("test-tenant");
        config.setConfigType("CONTRACT"); // Add configType
        fieldConfigRepository.save(config);

        ContractExtendField extendField = new ContractExtendField();
        extendField.setFieldId("ext-1");
        extendField.setFieldCode("project_code");
        extendField.setFieldName("Project Code");
        extendField.setFieldType("TEXT");
        extendField.setTenantId("test-tenant");
        extendFieldRepository.save(extendField);

        List<FieldMetadataDTO> fields = metadataService.getContractFields();

        FieldMetadataDTO contractNoField = fields.stream()
                .filter(f -> f.getFieldCode().equals("contract_no"))
                .findFirst().orElseThrow();
        assertThat(contractNoField.getFieldName()).isEqualTo("Custom Contract No"); // Corrected back to getFieldName()
        assertThat(contractNoField.getIsVisible()).isFalse();

        FieldMetadataDTO projectCodeField = fields.stream()
                .filter(f -> f.getFieldCode().equals("project_code"))
                .findFirst().orElseThrow();
        assertThat(projectCodeField.getSource()).isEqualTo("EXTEND");
        assertThat(projectCodeField.getFieldName()).isEqualTo("Project Code");
    }

    @Test
    void testSnakeCaseConversion() {
        List<FieldMetadataDTO> fields = metadataService.getContractFields();
        
        assertThat(fields.stream().anyMatch(f -> f.getFieldCode().equals("contract_no"))).isTrue();
        assertThat(fields.stream().anyMatch(f -> f.getFieldCode().equals("party_a_name"))).isTrue();
        assertThat(fields.stream().anyMatch(f -> f.getFieldCode().equals("total_amount_with_tax"))).isTrue();
    }
}
