package com.contract.master.evaluation.service;

import com.contract.master.domain.*;
import com.contract.master.service.AuditService;
import com.contract.master.security.TenantContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EvaluationService {

    @Autowired
    private EvaluationJobRepository evaluationJobRepository;

    @Autowired
    private ContractBaseRepository contractBaseRepository;

    @Autowired
    private RuleConfigRepository ruleConfigRepository;

    @Autowired
    private AuditService auditService;

    // Assuming a Kafka sender service exists for publishing events
    // @Autowired
    // private KafkaSenderService kafkaSenderService;

    @Transactional
    public String triggerEvaluation(String contractId, List<String> ruleIds, String triggeredBy) {
        String tenantId = TenantContext.getCurrentTenant();

        // 1. Validate contract and rules existence
        ContractBase contract = contractBaseRepository.findById(contractId)
                .orElseThrow(() -> new IllegalArgumentException("Contract not found: " + contractId));

        List<RuleConfig> rules = ruleConfigRepository.findAllById(ruleIds);
        if (rules.size() != ruleIds.size()) {
            throw new IllegalArgumentException("One or more rules not found.");
        }
        // Ensure rules belong to the same tenant
        boolean allRulesBelongToTenant = rules.stream()
                .allMatch(rule -> tenantId.equals(rule.getTenantId()));
        if (!allRulesBelongToTenant) {
            throw new SecurityException("Attempt to use rules from another tenant.");
        }

        // 2. Check for existing in-progress evaluations for the contract (FR-003, handle conflict)
        List<EvaluationJob> inProgressJobs = evaluationJobRepository.findByContractIdAndStatus(contractId, "IN_PROGRESS");
        if (!inProgressJobs.isEmpty()) {
            throw new IllegalStateException("An evaluation for this contract is already in progress.");
        }

        // 3. Create and save a new EvaluationJob entity with PENDING status
        EvaluationJob job = new EvaluationJob();
        job.setJobId(UUID.randomUUID().toString());
        job.setContractId(contractId);
        job.setRuleIds(ruleIds);
        job.setStatus("PENDING");
        job.setTriggeredBy(triggeredBy);
        job.setCreatedAt(LocalDateTime.now());
        job.setTenantId(tenantId);
        evaluationJobRepository.save(job);

        // 4. Publish a Kafka event to trigger the actual evaluation process (async)
        // kafkaSenderService.sendEvaluationTrigger(job.getJobId(), contractId, ruleIds, tenantId);
        System.out.println("Kafka event for evaluation trigger simulated for Job ID: " + job.getJobId()); // Placeholder

        // 5. Record audit log entry (FR-008)
        auditService.logReEvaluationTriggered(contractId, String.join(",", ruleIds), triggeredBy);

        return job.getJobId();
    }
}