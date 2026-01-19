package com.contract.master.evaluation.domain.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

import com.contract.master.shared.domain.model.BaseTenantEntity;
import com.contract.master.shared.domain.model.TenantId;

@Entity
@Table(name = "evaluation_results")
public class EvaluationResult extends BaseTenantEntity {

    @Column(name = "result_id", unique = true)
    private String resultId;

    @Column(name = "job_id", nullable = false)
    private String jobId;

    @Column(name = "rule_id", nullable = false)
    private String ruleId;

    @Column(name = "contract_id", nullable = false)
    private String contractId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ResultStatus status;

    @Column(name = "details", columnDefinition = "TEXT")
    private String details;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    public EvaluationResult() {}

    public EvaluationResult(TenantId tenantId, String jobId, String ruleId, String contractId, ResultStatus status, String details) {
        setTenantId(tenantId);
        this.jobId = jobId;
        this.ruleId = ruleId;
        this.contractId = contractId;
        this.status = status;
        this.details = details;
        this.timestamp = LocalDateTime.now();
    }

    public static EvaluationResult pass(TenantId tenantId, String jobId, String ruleId, String contractId, String details) {
        return new EvaluationResult(tenantId, jobId, ruleId, contractId, ResultStatus.PASS, details);
    }

    public static EvaluationResult fail(TenantId tenantId, String jobId, String ruleId, String contractId, String details) {
        return new EvaluationResult(tenantId, jobId, ruleId, contractId, ResultStatus.FAIL, details);
    }

    public String getResultId() { return resultId; }
    public void setResultId(String resultId) { this.resultId = resultId; }
    public String getJobId() { return jobId; }
    public void setJobId(String jobId) { this.jobId = jobId; }
    public String getRuleId() { return ruleId; }
    public void setRuleId(String ruleId) { this.ruleId = ruleId; }
    public String getContractId() { return contractId; }
    public void setContractId(String contractId) { this.contractId = contractId; }
    public ResultStatus getStatus() { return status; }
    public void setStatus(ResultStatus status) { this.status = status; }
    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
