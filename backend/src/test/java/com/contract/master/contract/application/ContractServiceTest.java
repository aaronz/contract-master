package com.contract.master.contract.application;
import com.contract.master.shared.domain.model.TenantId;

import com.contract.master.contract.domain.model.*;
import com.contract.master.contract.domain.repository.ContractRepository;
import com.contract.master.contract.domain.repository.ContractExtendDataRepository;
import com.contract.master.contract.domain.repository.ContractExtendFieldRepository;
import com.contract.master.contract.domain.repository.ContractAttachmentRepository;
import com.contract.master.contract.dto.ContractDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.context.ApplicationEventPublisher;
import com.contract.master.contract.domain.event.ContractSavedEvent;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import com.contract.master.audit.application.AuditService;

@ExtendWith(MockitoExtension.class)
public class ContractServiceTest {

    @Mock
    private ContractRepository contractRepository;

    @Mock
    private ContractExtendDataRepository extendDataRepository;

    @Mock
    private ContractExtendFieldRepository extendFieldRepository;

    @Mock
    private com.contract.master.contract.metadata.domain.repository.FieldConfigRepository fieldConfigRepository;

    @Mock
    private ContractAttachmentRepository attachmentRepository;

    @Mock
    private AuditService auditService;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private ContractService contractService;

    private Contract contract;

    @BeforeEach
    void setUp() {
        contract = new Contract();
        contract.setContractId(ContractId.of("550e8400-e29b-41d4-a716-446655440000"));
        contract.setContractNo(new ContractNo("TEST-NO-001"));
        contract.setContractName("Test Contract");
        contract.setAmount(ContractAmount.of(new BigDecimal("1000.00"), "USD"));
        contract.setTenantId(TenantId.of("tenant-1"));
    }

    @Test
    void testGetContractById() {
        when(contractRepository.findById(ContractId.of("550e8400-e29b-41d4-a716-446655440000"))).thenReturn(Optional.of(contract));
        
        ContractDTO dto = contractService.getContractById("550e8400-e29b-41d4-a716-446655440000");
        
        assertNotNull(dto);
        assertEquals("550e8400-e29b-41d4-a716-446655440000", dto.getContractId());
        assertEquals("Test Contract", dto.getContractName());
        verify(contractRepository).findById(ContractId.of("550e8400-e29b-41d4-a716-446655440000"));
    }

    @Test
    void testUpdateContract() {
        when(contractRepository.findById(ContractId.of("550e8400-e29b-41d4-a716-446655440000"))).thenReturn(Optional.of(contract));
        
        Contract updated = new Contract();
        updated.setContractName("Updated Name");
        updated.setAmount(ContractAmount.of(new BigDecimal("2000.00"), "USD"));
        
        contractService.updateContract("550e8400-e29b-41d4-a716-446655440000", updated);
        
        assertEquals("Updated Name", contract.getContractName());
        assertEquals(new BigDecimal("2000.00"), contract.getAmount().getAmount());
        verify(auditService).logChange(eq("550e8400-e29b-41d4-a716-446655440000"), eq("contract_name"), any(), eq("Updated Name"), eq("MANUAL"), eq("admin"));
        verify(contractRepository).save(contract);
        verify(eventPublisher).publishEvent(any(ContractSavedEvent.class));
    }

    @Test
    void testGetContractNotFound() {
        when(contractRepository.findById(ContractId.of("00000000-0000-0000-0000-000000000000"))).thenReturn(Optional.empty());
        
        ContractDTO dto = contractService.getContractById("00000000-0000-0000-0000-000000000000");
        
        assertNull(dto);
    }
}
