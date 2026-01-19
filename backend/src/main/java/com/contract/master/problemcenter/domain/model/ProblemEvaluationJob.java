package com.contract.master.problemcenter.domain.model;

import com.contract.master.shared.domain.base.AggregateRoot;
import com.contract.master.shared.domain.base.BaseDomainEntity;
import com.contract.master.shared.domain.model.TenantId;
import com.contract.master.shared.domain.model.BaseTenantEntity;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "problem_evaluation_jobs")
public class ProblemEvaluationJob extends BaseTenantEntity implements AggregateRoot {

    @Column(name = "contract_id", nullable = false)
    private UUID contractId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ProblemEvaluationJobStatus status = ProblemEvaluationJobStatus.PENDING;

    @Column(name = "triggered_by")
    private String triggeredBy;

    @Column(name = "error_details")
    private String errorDetails;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "finished_at")
    private LocalDateTime finishedAt;

    @Column(name = "rule_ids_json", columnDefinition = "TEXT")
    private String ruleIdsJson;

    public ProblemEvaluationJob() {}

    public UUID getContractId() { return contractId; }
    public void setContractId(UUID contractId) { this.contractId = contractId; }
    public ProblemEvaluationJobStatus getStatus() { return status; }
    public void setStatus(ProblemEvaluationJobStatus status) { this.status = status; }
    public String getTriggeredBy() { return triggeredBy; }
    public void setTriggeredBy(String triggeredBy) { this.triggeredBy = triggeredBy; }
    public String getErrorDetails() { return errorDetails; }
    public void setErrorDetails(String errorDetails) { this.errorDetails = errorDetails; }
    public LocalDateTime getStartedAt() { return startedAt; }
    public void setStartedAt(LocalDateTime startedAt) { this.startedAt = startedAt; }
    public LocalDateTime getFinishedAt() { return finishedAt; }
    public void setFinishedAt(LocalDateTime finishedAt) { this.finishedAt = finishedAt; }
    public String getRuleIdsJson() { return ruleIdsJson; }
    public void setRuleIdsJson(String ruleIdsJson) { this.ruleIdsJson = ruleIdsJson; }

    public void start() {
        this.status = ProblemEvaluationJobStatus.RUNNING;
        this.startedAt = LocalDateTime.now();
    }

    public void complete() {
        this.status = ProblemEvaluationJobStatus.COMPLETED;
        this.finishedAt = LocalDateTime.now();
    }

    public void fail(String details) {
        this.status = ProblemEvaluationJobStatus.FAILED;
        this.errorDetails = details;
        this.finishedAt = LocalDateTime.now();
    }
}
