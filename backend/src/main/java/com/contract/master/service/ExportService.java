package com.contract.master.service;

import com.contract.master.dto.ContractDTO;
import com.contract.master.domain.ContractBase;
import com.contract.master.domain.ContractBaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.PrintWriter;
import java.util.List;

@Service
public class ExportService {

    @Autowired
    private ContractService contractService;

    public void exportToCsv(PrintWriter writer) {
        List<ContractDTO> contracts = contractService.getAllContracts();
        writer.println("Contract No,Name,Party A,Party B,Amount,Status");
        for (ContractDTO c : contracts) {
            writer.println(String.format("%s,%s,%s,%s,%s,%s",
                    c.getContractNo(),
                    c.getContractName(),
                    c.getPartyAName(),
                    c.getPartyBName(),
                    c.getContractAmount(),
                    c.getContractStatus()));
        }
    }

    public byte[] exportToPdf(String contractId) {
        return "MOCK PDF CONTENT WITH WATERMARK".getBytes();
    }

    public byte[] exportToExcel() {
        return "MOCK EXCEL CONTENT".getBytes();
    }
}
