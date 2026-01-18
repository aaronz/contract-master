package com.contract.master.contract.metadata.application;

import com.contract.master.shared.domain.model.TenantId;
import com.contract.master.contract.domain.model.ContractExtendField;
import com.contract.master.contract.domain.repository.ContractExtendFieldRepository;
import com.contract.master.contract.metadata.domain.model.FieldConfig;
import com.contract.master.contract.metadata.domain.repository.FieldConfigRepository;
import com.contract.master.contract.metadata.dto.FieldMetadataDTO;
import com.contract.master.security.TenantContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MetadataServiceTest {

    @Mock
    private ContractExtendFieldRepository extendFieldRepository;
    @Mock
    private FieldConfigRepository fieldConfigRepository;

    @InjectMocks
    private MetadataService metadataService;

    @BeforeEach
    void setUp() {
        TenantContext.setCurrentTenant("tenant-1");
    }

    @AfterEach
    void tearDown() {
        TenantContext.clear();
    }

    @Test
    void testGetContractFields() {
        ContractExtendField field = new ContractExtendField();
        field.setFieldCode("custom_1");
        field.setFieldName("Custom Field 1");
        field.setFieldType("TEXT");
        
        when(extendFieldRepository.findByTenantId(eq(TenantId.of("tenant-1"))))
                .thenReturn(Collections.singletonList(field));

        FieldConfig contractNoConfig = new FieldConfig();
        contractNoConfig.setFieldCode("contract_no");
        contractNoConfig.setFieldAlias("Contract No");
        contractNoConfig.setConfigType("CONTRACT");
        when(fieldConfigRepository.findByTenantId(eq(TenantId.of("tenant-1"))))
                .thenReturn(Collections.singletonList(contractNoConfig));

        List<FieldMetadataDTO> result = metadataService.getContractFields();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        
        assertTrue(result.stream().anyMatch(f -> "contract_no".equals(f.getFieldCode())));
        
        assertTrue(result.stream().anyMatch(f -> "custom_1".equals(f.getFieldCode())));
        
        verify(extendFieldRepository).findByTenantId(eq(TenantId.of("tenant-1")));
    }
}
