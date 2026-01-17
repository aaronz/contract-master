package com.contract.master.contract.application;

import com.contract.master.integration.domain.model.DownstreamSystem;
import com.contract.master.integration.domain.repository.DownstreamSystemRepository;
import com.contract.master.dto.ContractDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PublishServiceTest {

    @Mock
    private DownstreamSystemRepository downstreamSystemRepository;

    @Mock
    private ContractService contractService;

    @InjectMocks
    private PublishService publishService;

    @Test
    void testGetPublishedData() {
        String accessKey = "valid-key";
        DownstreamSystem system = new DownstreamSystem();
        system.setAccessKey(accessKey);
        system.setIsEnabled(true);

        when(downstreamSystemRepository.findByAccessKeyAndIsEnabledTrue(accessKey))
                .thenReturn(Optional.of(system));

        ContractDTO c1 = new ContractDTO();
        c1.setContractStatus("PUBLISHED");
        ContractDTO c2 = new ContractDTO();
        c2.setContractStatus("DRAFT");

        when(contractService.getAllContracts()).thenReturn(Arrays.asList(c1, c2));

        List<ContractDTO> result = publishService.getPublishedData(accessKey);

        assertEquals(1, result.size());
        assertEquals("PUBLISHED", result.get(0).getContractStatus());
    }

    @Test
    void testGetPublishedDataInvalidKey() {
        when(downstreamSystemRepository.findByAccessKeyAndIsEnabledTrue("invalid")).thenReturn(Optional.empty());
        
        assertThrows(RuntimeException.class, () -> publishService.getPublishedData("invalid"));
    }
}
