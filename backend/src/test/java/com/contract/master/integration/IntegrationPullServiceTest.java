package com.contract.master.integration;

import com.contract.master.contract.domain.model.Contract;
import com.contract.master.contract.domain.model.ContractId;
import com.contract.master.contract.domain.model.ContractNo;
import com.contract.master.contract.domain.repository.ContractRepository;
import com.contract.master.integration.application.IntegrationPullService;
import com.contract.master.integration.application.IntegrationPushService;
import com.contract.master.integration.domain.model.FieldMapping;
import com.contract.master.integration.domain.repository.FieldMappingRepository;
import com.contract.master.integration.domain.repository.IntegrationLogRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class IntegrationPullServiceTest {

    @Mock
    private ContractRepository contractRepository;

    @Mock
    private FieldMappingRepository mappingRepository;

    @Mock
    private IntegrationLogRepository logRepository;

    @Mock
    private IntegrationPushService pushService;

    @InjectMocks
    private IntegrationPullService pullService;

    @Test
    void shouldPullAndTransformContracts() {
        // Given
        Contract contract = new Contract();
        contract.setContractId(ContractId.of(UUID.randomUUID().toString()));
        contract.setContractNo(new ContractNo("CON-001"));
        
        FieldMapping mapping = new FieldMapping();
        mapping.setInternalField("contractName");
        mapping.setExternalField("legal_title");

        when(contractRepository.findAll(any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(contract)));
        
        when(mappingRepository.findByTargetSystemIdAndDirectionAndIsEnabledTrue(anyString(), eq("OUTBOUND")))
                .thenReturn(List.of(mapping));
        
        when(pushService.transformData(any(), anyList()))
                .thenReturn(Map.of("legal_title", "Contract Title"));

        // When
        List<Map<String, Object>> result = pullService.pullContracts("test-system", null, Pageable.unpaged());

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).containsKey("legal_title");
        assertThat(result.get(0).get("legal_title")).isEqualTo("Contract Title");
    }
}
