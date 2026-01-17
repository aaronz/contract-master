package com.contract.master.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "evaluation_jobs")
@Data
@EqualsAndHashCode(callSuper = true)
@EntityListeners({TenantEntityListener.class})
public class EvaluationJob extends BaseTenantEntity {
    @Id
    @Column(name = "job_id", length = 64)
    private String jobId;

    @Column(name = "contract_id", length = 64, nullable = false)
    private String contractId;

    @ElementCollection
    @CollectionTable(name = "evaluation_job_rules", joinColumns = @JoinColumn(name = "job_id"))
    @Column(name = "rule_id", length = 64)
    private List<String> ruleIds;

    @Column(name = "status", length = 32, nullable = false)
    private String status; // PENDING, IN_PROGRESS, COMPLETED, FAILED

    @Column(name = "triggered_by", length = 64, nullable = false)
    private String triggeredBy; // User ID

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "results", columnDefinition = "jsonb")
    private Map<String, Object> results;

    @Override
    public String getTenantId() {
        return super.getTenantId();
    }

    @Override
    public void setTenantId(String tenantId) {
        super.setTenantId(tenantId);
    }
}