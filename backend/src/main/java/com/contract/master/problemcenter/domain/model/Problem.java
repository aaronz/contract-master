package com.contract.master.problemcenter.domain.model;

import com.contract.master.shared.domain.base.BaseDomainEntity;
import com.contract.master.shared.domain.model.TenantId;
import com.contract.master.rule.domain.model.Severity;
import com.contract.master.shared.domain.model.BaseTenantEntity;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "problems")
public class Problem extends BaseTenantEntity {

    @Column(name = "evaluation_job_id", nullable = false)
    private Long evaluationJobId;

    @Column(name = "rule_id", nullable = false)
    private Long ruleId;

    @Column(name = "contract_id", nullable = false)
    private UUID contractId;

    @Column(name = "location_in_contract")
    @JdbcTypeCode(SqlTypes.JSON)
    private String locationInContract;

    @Column(name = "highlighted_text")
    private String highlightedText;

    @Column(name = "generated_message")
    private String generatedMessage;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ProblemStatus status = ProblemStatus.NEW;

    @Column(name = "assignee_id")
    private String assigneeId;

    @Column(name = "notes")
    private String notes;

    @Column(name = "severity")
    @Enumerated(EnumType.STRING)
    private Severity severity;

    public Problem() {}

    public Long getEvaluationJobId() { return evaluationJobId; }
    public void setEvaluationJobId(Long evaluationJobId) { this.evaluationJobId = evaluationJobId; }
    public Long getRuleId() { return ruleId; }
    public void setRuleId(Long ruleId) { this.ruleId = ruleId; }
    public UUID getContractId() { return contractId; }
    public void setContractId(UUID contractId) { this.contractId = contractId; }
    public String getLocationInContract() { return locationInContract; }
    public void setLocationInContract(String locationInContract) { this.locationInContract = locationInContract; }
    public String getHighlightedText() { return highlightedText; }
    public void setHighlightedText(String highlightedText) { this.highlightedText = highlightedText; }
    public String getGeneratedMessage() { return generatedMessage; }
    public void setGeneratedMessage(String generatedMessage) { this.generatedMessage = generatedMessage; }
    public ProblemStatus getStatus() { return status; }
    public void setStatus(ProblemStatus status) { this.status = status; }
    public String getAssigneeId() { return assigneeId; }
    public void setAssigneeId(String assigneeId) { this.assigneeId = assigneeId; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public Severity getSeverity() { return severity; }
    public void setSeverity(Severity severity) { this.severity = severity; }
}
