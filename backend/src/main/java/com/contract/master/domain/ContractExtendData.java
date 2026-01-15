package com.contract.master.domain;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "contract_extend_data")
@Data
@EntityListeners(AuditingEntityListener.class)
public class ContractExtendData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "contract_id", length = 64)
    private String contractId;

    @Column(name = "field_id", length = 64)
    private String fieldId;

    @Column(name = "field_value", columnDefinition = "TEXT")
    private String fieldValue;

    @Column(name = "fill_type", length = 32)
    private String fillType;

    @Column(name = "fill_user", length = 64)
    private String fillUser;

    @Column(name = "fill_time")
    private LocalDateTime fillTime;

    public String getFieldId() { return fieldId; }
    public void setFieldId(String fieldId) { this.fieldId = fieldId; }
    public String getFieldValue() { return fieldValue; }
    public void setFieldValue(String fieldValue) { this.fieldValue = fieldValue; }
    public void setContractId(String contractId) { this.contractId = contractId; }
    public void setFillType(String fillType) { this.fillType = fillType; }
}
