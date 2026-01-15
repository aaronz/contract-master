package com.contract.master.service;

import com.contract.master.domain.ContractExtendField;
import com.contract.master.domain.ContractExtendFieldRepository;
import com.contract.master.dto.ContractDTO;
import com.contract.master.security.TenantContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ExportService {

    @Autowired
    private ContractService contractService;

    @Autowired
    private ContractExtendFieldRepository extendFieldRepository;

    public void exportToCsv(java.io.PrintWriter writer) {
        String tenantId = TenantContext.getCurrentTenant();
        List<ContractDTO> contracts = contractService.getAllContracts().stream()
                .filter(c -> tenantId.equals(c.getTenantId()))
                .collect(Collectors.toList());

        List<ContractExtendField> extendFields = extendFieldRepository.findByTenantId(tenantId);

        writer.print("Contract No,Contract Name,Party A,Party B,Amount,Status");
        for (ContractExtendField f : extendFields) {
            writer.print("," + f.getFieldName());
        }
        writer.println();

        for (ContractDTO c : contracts) {
            writer.print(c.getContractNo() + ",");
            writer.print(c.getContractName() + ",");
            writer.print(c.getPartyAName() + ",");
            writer.print(c.getPartyBName() + ",");
            writer.print(c.getContractAmount() + ",");
            writer.print(c.getContractStatus());
            
            Map<String, Object> ext = c.getExtendedFields();
            for (ContractExtendField f : extendFields) {
                writer.print("," + (ext != null ? ext.getOrDefault(f.getFieldCode(), "") : ""));
            }
            writer.println();
        }
    }

    public byte[] exportToPdf(String id) {
        return new byte[0];
    }
}
