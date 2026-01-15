package com.contract.master.domain;

import com.contract.master.domain.BaseTenantEntity;
import com.contract.master.domain.TenantEntityListener;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "contract_extend_field")
@Data
@EntityListeners({AuditingEntityListener.class, TenantEntityListener.class})
public class ContractExtendField extends BaseTenantEntity {
    @Override
    public String getTenantId() { return super.getTenantId(); }
    @Override
    public void setTenantId(String tenantId) { super.setTenantId(tenantId); }
    @Id
    @Column(name = "field_id", length = 64)
    private String fieldId;

    @Column(name = "field_name", length = 64)
    private String fieldName;

    @Column(name = "field_code", length = 64)
    private String fieldCode;

    @Column(name = "field_type", length = 32)
    private String fieldType;

    @Column(name = "is_required")
    private Boolean isRequired;

    @Column(name = "default_value", length = 255)
    private String defaultValue;

    @Column(name = "remark", length = 512)
    private String remark;

    @CreatedDate
    @Column(name = "create_time", updatable = false)
    private LocalDateTime createTime;

    @LastModifiedDate
    @Column(name = "update_time")
    private LocalDateTime updateTime;

    public String getFieldId() { return fieldId; }
    public void setFieldId(String fieldId) { this.fieldId = fieldId; }
    public String getFieldCode() { return fieldCode; }
    public void setFieldCode(String fieldCode) { this.fieldCode = fieldCode; }
}
