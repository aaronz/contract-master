package com.contract.master.evaluation.domain.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

import com.contract.master.shared.domain.base.BaseDomainEntity;

@Entity
@Table(name = "evaluation_results")
public class EvaluationResult extends BaseDomainEntity {

    @Column(name = "result_id", unique = true)
    private String resultId;

    @Column(name = "tenant_id", nullable = false)
    private String tenantId;

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

    public EvaluationResult(String tenantId, String jobId, String ruleId, String contractId, ResultStatus status, String details) {
        this.tenantId = tenantId;
        this.jobId = jobId;
        this.ruleId = ruleId;
        this.contractId = contractId;
        this.status = status;
        this.details = details;
        this.timestamp = LocalDateTime.now();
    }

    public static EvaluationResult pass(String tenantId, String jobId, String ruleId, String contractId, String details) {
        return new EvaluationResult(tenantId, jobId, ruleId, contractId, ResultStatus.PASS, details);
    }

    public static EvaluationResult fail(String tenantId, String jobId, String ruleId, String contractId, String details) {
        return new EvaluationResult(tenantId, jobId, ruleId, contractId, ResultStatus.FAIL, details);
    }

    public String getResultId() { return resultId; }
    public void setResultId(String resultId) { this.resultId = resultId; }
    public String getTenantId() { return tenantId; }
    public String getJobId() { return jobId; }
    public String getRuleId() { return ruleId; }
    public String getContractId() { return contractId; }
    public ResultStatus getStatus() { return status; }
    public String getDetails() { return details; }
    public LocalDateTime getTimestamp() { return timestamp; }

    public enum ResultStatus {
        PASS, FAIL
    }
}
