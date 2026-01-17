package com.contract.master.evaluation.domain.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "evaluation_jobs")
public class EvaluationJob {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "tenant_id", nullable = false)
    private String tenantId;

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

    // Constructors
    public EvaluationJob() {
    }

    public EvaluationJob(String tenantId, JobStatus status, TriggerType triggerType, LocalDateTime createdAt, String triggeredBy) {
        this.tenantId = tenantId;
        this.status = status;
        this.triggerType = triggerType;
        this.createdAt = createdAt;
        this.triggeredBy = triggeredBy;
    }

    // Getters and Setters
    public String getTargetRules() {
        return targetRules;
    }

    public void setTargetRules(String targetRules) {
        this.targetRules = targetRules;
    }

    public String getTargetContracts() {
        return targetContracts;
    }

    public void setTargetContracts(String targetContracts) {
        this.targetContracts = targetContracts;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public JobStatus getStatus() {
        return status;
    }

    public void setStatus(JobStatus status) {
        this.status = status;
    }

    public TriggerType getTriggerType() {
        return triggerType;
    }

    public void setTriggerType(TriggerType triggerType) {
        this.triggerType = triggerType;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }

    public String getTriggeredBy() {
        return triggeredBy;
    }

    public void setTriggeredBy(String triggeredBy) {
        this.triggeredBy = triggeredBy;
    }

    // Enums
    public enum JobStatus {
        PENDING, IN_PROGRESS, COMPLETED, FAILED
    }

    public enum TriggerType {
        MANUAL, AUTOMATIC
    }
}
