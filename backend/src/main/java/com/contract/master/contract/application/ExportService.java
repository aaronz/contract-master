package com.contract.master.contract.application;

import com.contract.master.contract.domain.model.ContractExtendField;
import com.contract.master.contract.domain.repository.ContractExtendFieldRepository;
import com.contract.master.contract.dto.ContractDTO;
import com.contract.master.security.TenantContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.contract.master.shared.domain.model.TenantId;
@Service
public class ExportService {

    @Autowired
    private ContractService contractService;

    @Autowired
    private ContractExtendFieldRepository extendFieldRepository;

    public void exportToCsv(java.io.PrintWriter writer) {
        List<ContractDTO> contracts = contractService.getAllContracts();
        List<ContractExtendField> extendFields = extendFieldRepository.findAll();

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
