package com.contract.master.contract.interfaces;

import com.contract.master.contract.application.ContractApplicationService;
import com.contract.master.contract.domain.model.Contract;
import com.contract.master.contract.domain.model.ContractId;
import com.contract.master.contract.domain.model.ContractNo;
import com.contract.master.shared.domain.model.TenantId;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;

@RestController("dddContractController")
@RequestMapping("/api/v1/contracts")
public class ContractController {
    private final ContractApplicationService contractApplicationService;

    public ContractController(ContractApplicationService contractApplicationService) {
        this.contractApplicationService = contractApplicationService;
    }

    @PostMapping
    public String create(@RequestParam String tenantId, @RequestParam String contractNo, @RequestParam String name) {
        ContractId id = contractApplicationService.createContract(
                TenantId.of(tenantId),
                new ContractNo(contractNo),
                name
        );
        return id.toString();
    }

    @PutMapping("/{id}")
    public void update(@PathVariable String id, @RequestParam(required = false) String name, @RequestParam(required = false) LocalDate signDate) {
        contractApplicationService.updateContract(ContractId.of(id), name, signDate);
    }

    @GetMapping("/{id}")
    public Contract get(@PathVariable String id) {
        return contractApplicationService.getContract(ContractId.of(id));
    }
}
