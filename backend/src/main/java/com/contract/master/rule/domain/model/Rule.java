package com.contract.master.rule.domain.model;

import com.contract.master.shared.domain.base.AggregateRoot;
import com.contract.master.shared.domain.base.BaseDomainEntity;
import com.contract.master.shared.domain.model.TenantId;
import com.contract.master.shared.domain.model.BaseTenantEntity;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "rules")
public class Rule extends BaseTenantEntity implements AggregateRoot {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "logic_content", nullable = false, columnDefinition = "TEXT")
    private String logicContent;

    @Enumerated(EnumType.STRING)
    @Column(name = "logic_type", nullable = false)
    private RuleLogicType logicType;

    @Enumerated(EnumType.STRING)
    @Column(name = "severity", nullable = false)
    private Severity severity;

    @Column(name = "category")
    private String category;

    @Column(name = "version", nullable = false)
    private Integer version = 1;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private RuleStatus status = RuleStatus.DRAFT;

    public Rule() {}

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getLogicContent() { return logicContent; }
    public void setLogicContent(String logicContent) { this.logicContent = logicContent; }
    public RuleLogicType getLogicType() { return logicType; }
    public void setLogicType(RuleLogicType logicType) { this.logicType = logicType; }
    public Severity getSeverity() { return severity; }
    public void setSeverity(Severity severity) { this.severity = severity; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public Integer getVersion() { return version; }
    public void setVersion(Integer version) { this.version = version; }
    public RuleStatus getStatus() { return status; }
    public void setStatus(RuleStatus status) { this.status = status; }
}
