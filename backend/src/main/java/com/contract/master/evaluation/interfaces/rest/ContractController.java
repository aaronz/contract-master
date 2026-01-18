package com.contract.master.evaluation.interfaces.rest;

import com.contract.master.contract.dto.ContractDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@RestController("evaluationContractController")
@RequestMapping("/api/evaluation/contracts")
public class ContractController {

    @GetMapping
    public ResponseEntity<List<ContractDTO>> getContracts() {
        // Mock data using the project's real ContractDTO
        ContractDTO c1 = new ContractDTO();
        c1.setContractId(UUID.randomUUID().toString());
        c1.setContractName("Employment Agreement v1");
        
        ContractDTO c2 = new ContractDTO();
        c2.setContractId(UUID.randomUUID().toString());
        c2.setContractName("NDA for Project X");
        
        return ResponseEntity.ok(Arrays.asList(c1, c2));
    }
}
