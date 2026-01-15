package com.contract.master.service;

import com.contract.master.dto.ContractDTO;
import com.contract.master.domain.DownstreamSystem;
import com.contract.master.domain.DownstreamSystemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PublishService {

    @Autowired
    private DownstreamSystemRepository downstreamSystemRepository;

    @Autowired
    private ContractService contractService;

    public List<ContractDTO> getPublishedData(String accessKey) {
        DownstreamSystem system = downstreamSystemRepository.findByAccessKeyAndIsEnabledTrue(accessKey)
                .orElseThrow(() -> new RuntimeException("Invalid or disabled access key"));

        return contractService.getAllContracts().stream()
                .filter(c -> "PUBLISHED".equals(c.getContractStatus()))
                .collect(Collectors.toList());
    }
}
