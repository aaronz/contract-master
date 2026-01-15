package com.contract.master.service;

import com.contract.master.domain.ContractExtendField;
import com.contract.master.domain.ContractExtendFieldRepository;
import com.contract.master.dto.FieldMetadataDTO;
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
        
        when(extendFieldRepository.findByTenantId("tenant-1"))
                .thenReturn(Collections.singletonList(field));

        List<FieldMetadataDTO> result = metadataService.getContractFields();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        
        assertTrue(result.stream().anyMatch(f -> "contractNo".equals(f.getFieldCode())));
        
        assertTrue(result.stream().anyMatch(f -> "custom_1".equals(f.getFieldCode())));
        
        verify(extendFieldRepository).findByTenantId("tenant-1");
    }
}
