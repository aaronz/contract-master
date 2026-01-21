package com.contract.master.contract.metadata.application;

import com.contract.master.contract.domain.model.ContractExtendField;
import com.contract.master.contract.domain.repository.ContractExtendFieldRepository;
import com.contract.master.contract.metadata.domain.event.FieldConfigChangedEvent;
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
    void shouldMergeStandardAndExtendedFields() {
        // Given
        ContractExtendField field = new ContractExtendField();
        field.setFieldCode("custom_field");
        field.setFieldName("Custom Label");
        field.setFieldType("TEXT");
        when(extendFieldRepository.findAll()).thenReturn(Collections.singletonList(field));
        when(fieldConfigRepository.findAll()).thenReturn(Collections.emptyList());

        // When
        List<FieldMetadataDTO> result = metadataService.getContractFields();

        // Then
        assertTrue(result.stream().anyMatch(f -> "contract_no".equals(f.getFieldCode()) && "STANDARD".equals(f.getSource())));
        assertTrue(result.stream().anyMatch(f -> "custom_field".equals(f.getFieldCode()) && "EXTEND".equals(f.getSource())));
    }

    @Test
    void shouldApplyFieldConfigOverrides() {
        // Given
        FieldConfig config = new FieldConfig();
        config.setFieldCode("contract_no");
        config.setFieldAlias("Custom Serial Number");
        config.setDisplayOrder(1);
        config.setIsVisible(true);
        when(fieldConfigRepository.findAll()).thenReturn(Collections.singletonList(config));
        when(extendFieldRepository.findAll()).thenReturn(Collections.emptyList());

        // When
        List<FieldMetadataDTO> result = metadataService.getContractFields();

        // Then
        FieldMetadataDTO contractNo = result.stream()
                .filter(f -> "contract_no".equals(f.getFieldCode()))
                .findFirst().orElseThrow();
        assertEquals("Custom Serial Number", contractNo.getFieldName());
        assertEquals(1, contractNo.getDisplayOrder());
    }

    @Test
    void shouldInvalidateCacheOnEvent() {
        // Given
        when(fieldConfigRepository.findAll()).thenReturn(Collections.emptyList());
        when(extendFieldRepository.findAll()).thenReturn(Collections.emptyList());
        metadataService.getContractFields();
        
        // When
        metadataService.handleFieldConfigChanged(new FieldConfigChangedEvent("tenant-1"));
        metadataService.getContractFields();

        // Then
        verify(fieldConfigRepository, times(2)).findAll();
    }
}
