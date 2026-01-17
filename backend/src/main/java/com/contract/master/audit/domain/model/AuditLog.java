package com.contract.master.audit.domain.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import com.contract.master.shared.domain.model.BaseTenantEntity;

@Entity
@Table(name = "audit_log")
@Data
@EntityListeners(AuditingEntityListener.class)
public class AuditLog extends BaseTenantEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "contract_id", length = 64)
    private String contractId;

    @Column(name = "field_name", length = 64)
    private String fieldName;

    @Column(name = "old_value", columnDefinition = "TEXT")
    private String oldValue;

    @Column(name = "new_value", columnDefinition = "TEXT")
    private String newValue;

    @Column(name = "modify_type", length = 32)
    private String modifyType;

    @Column(name = "modify_user", length = 64)
    private String modifyUser;

    @Column(name = "details", columnDefinition = "TEXT")
    private String details;

    @CreatedDate
    @Column(name = "create_time", updatable = false)
    private LocalDateTime createTime;

    public void setContractId(String contractId) { this.contractId = contractId; }
    public void setFieldName(String fieldName) { this.fieldName = fieldName; }
    public void setOldValue(String oldValue) { this.oldValue = oldValue; }
    public void setNewValue(String newValue) { this.newValue = newValue; }
    public void setModifyType(String modifyType) { this.modifyType = modifyType; }
    public void setModifyUser(String modifyUser) { this.modifyUser = modifyUser; }
    public void setDetails(String details) { this.details = details; }
    public String getDetails() { return details; }
}
