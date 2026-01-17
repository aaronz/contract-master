package com.contract.master.evaluation.interfaces.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import com.contract.master.contract.application.ContractService;
import com.contract.master.dto.ContractDTO;

@RestController("evaluationContractController")
@RequestMapping("/api/evaluation/contracts")
public class ContractController {

    // Placeholder for a Contract DTO
    public static class ContractDTO {
        private String id;
        private String name;

        public ContractDTO(String id, String name) {
            this.id = id;
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    @GetMapping
    public ResponseEntity<List<ContractDTO>> getContracts() {
        // In a real application, this would fetch contracts from a ContractService/Repository
        // For now, return mock data.
        List<ContractDTO> contracts = Arrays.asList(
            new ContractDTO(UUID.randomUUID().toString(), "Employment Agreement v1"),
            new ContractDTO(UUID.randomUUID().toString(), "NDA for Project X"),
            new ContractDTO(UUID.randomUUID().toString(), "Service Level Agreement 2024"),
            new ContractDTO(UUID.randomUUID().toString(), "Vendor Contract with Acme Corp")
        );
        return ResponseEntity.ok(contracts);
    }
}
