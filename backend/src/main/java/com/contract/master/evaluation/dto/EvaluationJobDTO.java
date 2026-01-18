package com.contract.master.evaluation.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class EvaluationJobDTO {
    private String jobId;
    private String tenantId;
    private String status;
    private String triggerType; // Corresponds to EvaluationJob.TriggerType
    private LocalDateTime createdAt;
    private LocalDateTime completedAt;
    private String triggeredBy;
    private List<String> targetRules; // Deserialized from target_rules JSON string
    private List<String> targetContracts; // Deserialized from target_contracts JSON string
    private Map<String, Object> results; // Not directly from model, but useful for UI

    public EvaluationJobDTO() {
    }

    public EvaluationJobDTO(String jobId, String tenantId, String status, String triggerType, LocalDateTime createdAt, LocalDateTime completedAt, String triggeredBy, List<String> targetRules, List<String> targetContracts, Map<String, Object> results) {
        this.jobId = jobId;
        this.tenantId = tenantId;
        this.status = status;
        this.triggerType = triggerType;
        this.createdAt = createdAt;
        this.completedAt = completedAt;
        this.triggeredBy = triggeredBy;
        this.targetRules = targetRules;
        this.targetContracts = targetContracts;
        this.results = results;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTriggerType() {
        return triggerType;
    }

    public void setTriggerType(String triggerType) {
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

    public List<String> getTargetRules() {
        return targetRules;
    }

    public void setTargetRules(List<String> targetRules) {
        this.targetRules = targetRules;
    }

    public List<String> getTargetContracts() {
        return targetContracts;
    }

    public void setTargetContracts(List<String> targetContracts) {
        this.targetContracts = targetContracts;
    }

    public Map<String, Object> getResults() {
        return results;
    }

    public void setResults(Map<String, Object> results) {
        this.results = results;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EvaluationJobDTO that = (EvaluationJobDTO) o;
        return Objects.equals(jobId, that.jobId) && Objects.equals(tenantId, that.tenantId) && Objects.equals(status, that.status) && Objects.equals(triggerType, that.triggerType) && Objects.equals(createdAt, that.createdAt) && Objects.equals(completedAt, that.completedAt) && Objects.equals(triggeredBy, that.triggeredBy) && Objects.equals(targetRules, that.targetRules) && Objects.equals(targetContracts, that.targetContracts) && Objects.equals(results, that.results);
    }

    @Override
    public int hashCode() {
        return Objects.hash(jobId, tenantId, status, triggerType, createdAt, completedAt, triggeredBy, targetRules, targetContracts, results);
    }

    @Override
    public String toString() {
        return "EvaluationJobDTO{" +
               "jobId='" + jobId + '\'' +
               ", tenantId='" + tenantId + '\'' +
               ", status='" + status + '\'' +
               ", triggerType='" + triggerType + '\'' +
               ", createdAt=" + createdAt +
               ", completedAt=" + completedAt +
               ", triggeredBy='" + triggeredBy + '\'' +
               ", targetRules=" + targetRules +
               ", targetContracts=" + targetContracts +
               ", results=" + results +
               '}';
    }
}