package com.contract.master.problemcenter.application;

import com.contract.master.audit.application.AuditService;
import com.contract.master.problemcenter.domain.model.ProblemEvaluationJob;
import com.contract.master.problemcenter.domain.repository.ProblemEvaluationJobRepository;
import com.contract.master.problemcenter.infrastructure.messaging.EvaluationProducer;
import com.contract.master.shared.domain.model.TenantId;
import com.contract.master.security.TenantContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class EvaluationService {

    private final ProblemEvaluationJobRepository jobRepository;
    private final EvaluationProducer evaluationProducer;
    private final AuditService auditService;

    public EvaluationService(ProblemEvaluationJobRepository jobRepository,
                              EvaluationProducer evaluationProducer,
                              AuditService auditService) {
        this.jobRepository = jobRepository;
        this.evaluationProducer = evaluationProducer;
        this.auditService = auditService;
    }

    @Transactional
    public Long startEvaluation(UUID contractId) {
        ProblemEvaluationJob job = new ProblemEvaluationJob();
        job.setContractId(contractId);
        job.setTenantId(TenantId.of(TenantContext.getCurrentTenant()));
        job.setTriggeredBy(TenantContext.getCurrentTenant());

        ProblemEvaluationJob saved = jobRepository.save(job);

        auditService.logReEvaluationTriggered(
                contractId.toString(),
                "all-active-rules",
                TenantContext.getCurrentTenant()
        );

        evaluationProducer.publishJob(saved.getId());
        return saved.getId();
    }
}
