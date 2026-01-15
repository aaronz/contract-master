package com.contract.master.service;

import com.contract.master.domain.ContractBase;
import com.contract.master.domain.ContractBaseRepository;
import com.contract.master.domain.ContractExtendDataRepository;
import com.contract.master.domain.ContractExtendFieldRepository;
import com.contract.master.dto.ContractDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ContractServiceTest {

    @Mock
    private ContractBaseRepository contractBaseRepository;

    @Mock
    private ContractExtendDataRepository extendDataRepository;

    @Mock
    private ContractExtendFieldRepository extendFieldRepository;

    @Mock
    private AuditService auditService;

    @InjectMocks
    private ContractService contractService;

    private ContractBase contract;

    @BeforeEach
    void setUp() {
        contract = new ContractBase();
        contract.setContractId("CON-001");
        contract.setContractName("Test Contract");
        contract.setAmount(new BigDecimal("1000.00"));
    }

    @Test
    void testGetContractById() {
        when(contractBaseRepository.findById("CON-001")).thenReturn(Optional.of(contract));
        
        ContractDTO dto = contractService.getContractById("CON-001");
        
        assertNotNull(dto);
        assertEquals("CON-001", dto.getContractId());
        assertEquals("Test Contract", dto.getContractName());
        verify(contractBaseRepository).findById("CON-001");
    }

    @Test
    void testUpdateContract() {
        when(contractBaseRepository.findById("CON-001")).thenReturn(Optional.of(contract));
        
        ContractBase updated = new ContractBase();
        updated.setContractName("Updated Name");
        updated.setAmount(new BigDecimal("2000.00"));
        
        contractService.updateContract("CON-001", updated);
        
        assertEquals("Updated Name", contract.getContractName());
        assertEquals(new BigDecimal("2000.00"), contract.getAmount());
        verify(auditService).logChange(eq("CON-001"), eq("contract_name"), any(), eq("Updated Name"), eq("MANUAL"), eq("admin"));
        verify(contractBaseRepository).save(contract);
    }

    @Test
    void testGetContractNotFound() {
        when(contractBaseRepository.findById("NON-EXISTENT")).thenReturn(Optional.empty());
        
        ContractDTO dto = contractService.getContractById("NON-EXISTENT");
        
        assertNull(dto);
    }
}
