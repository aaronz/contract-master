package com.contract.master.evaluation.domain.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

import com.contract.master.shared.domain.model.BaseTenantEntity;
import com.contract.master.shared.domain.model.TenantId;

@Entity
@Table(name = "evaluation_jobs")
public class EvaluationJob extends BaseTenantEntity {

    @Column(name = "job_id", unique = true)
    private String jobId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private JobStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "trigger_type", nullable = false)
    private TriggerType triggerType;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "triggered_by")
    private String triggeredBy;

    @Column(name = "target_rules", length = 4096)
    private String targetRules;

    @Column(name = "target_contracts", length = 4096)
    private String targetContracts;

    public EvaluationJob() {}

    public EvaluationJob(String tenantId, TriggerType triggerType, String triggeredBy) {
        setTenantId(TenantId.of(tenantId));
        this.status = JobStatus.PENDING;
        this.triggerType = triggerType;
        this.createdAt = LocalDateTime.now();
        this.triggeredBy = triggeredBy;
    }

    public EvaluationJob(String tenantId, JobStatus status, TriggerType triggerType, LocalDateTime createdAt, String triggeredBy) {
        setTenantId(TenantId.of(tenantId));
        this.status = status;
        this.triggerType = triggerType;
        this.createdAt = createdAt;
        this.triggeredBy = triggeredBy;
    }

    public void start() {
        if (this.status != JobStatus.PENDING) {
            throw new IllegalStateException("Only pending jobs can be started");
        }
        this.status = JobStatus.IN_PROGRESS;
    }

    public void complete() {
        this.status = JobStatus.COMPLETED;
        this.completedAt = LocalDateTime.now();
    }

    public void fail() {
        this.status = JobStatus.FAILED;
        this.completedAt = LocalDateTime.now();
    }

    public String getJobId() { return jobId; }
    public void setJobId(String jobId) { this.jobId = jobId; }
    public JobStatus getStatus() { return status; }
    public void setStatus(JobStatus status) { this.status = status; }
    public TriggerType getTriggerType() { return triggerType; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }
    public String getTriggeredBy() { return triggeredBy; }
    public String getTargetRules() { return targetRules; }
    public void setTargetRules(String targetRules) { this.targetRules = targetRules; }
    public String getTargetContracts() { return targetContracts; }
    public void setTargetContracts(String targetContracts) { this.targetContracts = targetContracts; }

    public enum JobStatus {
        PENDING, IN_PROGRESS, COMPLETED, FAILED
    }

    public enum TriggerType {
        MANUAL, SCHEDULED, API
    }
}
