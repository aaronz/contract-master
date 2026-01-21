package com.contract.master.integration.application;

import com.contract.master.contract.application.ContractService;
import com.contract.master.contract.domain.model.Contract;
import com.contract.master.contract.domain.repository.ContractRepository;
import com.contract.master.integration.domain.model.FieldMapping;
import com.contract.master.integration.domain.model.IntegrationLog;
import com.contract.master.integration.domain.repository.FieldMappingRepository;
import com.contract.master.integration.domain.repository.IntegrationLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class IntegrationPullService {

    @Autowired
    private ContractRepository contractRepository;

    @Autowired
    private FieldMappingRepository mappingRepository;

    @Autowired
    private IntegrationLogRepository logRepository;

    @Autowired
    private IntegrationPushService pushService;

    @Transactional(readOnly = true)
    public List<Map<String, Object>> pullContracts(String systemId, LocalDateTime updatedSince, Pageable pageable) {
        Page<Contract> contracts;
        if (updatedSince != null) {
            contracts = contractRepository.findByUpdateTimeAfter(updatedSince, pageable);
        } else {
            contracts = contractRepository.findAll(pageable);
        }
        
        List<FieldMapping> mappings = mappingRepository.findByTargetSystemIdAndDirectionAndIsEnabledTrue(systemId, "OUTBOUND");

        return contracts.getContent().stream()
                .map(contract -> pushService.transformData(contract, mappings))
                .collect(Collectors.toList());
    }

    @Transactional
    public void logPullEvent(String systemName, int count) {
        IntegrationLog log = new IntegrationLog();
        log.setSourceSystem(systemName);
        log.setEventType("INBOUND_PULL");
        log.setRecordsCount(count);
        log.setStatus("SUCCESS");
        logRepository.save(log);
    }
}
