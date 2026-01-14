package com.contract.master.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "contract_change_record")
@Data
@EqualsAndHashCode(callSuper = true)
public class ContractChangeRecord extends BaseTenantEntity {
    @Id
    @Column(name = "change_id", length = 64)
    private String changeId;

    @Column(name = "contract_id", length = 64)
    private String contractId;

    @Column(name = "change_type", length = 32)
    private String changeType;

    @Column(name = "change_content", columnDefinition = "TEXT")
    private String changeContent;

    @Column(name = "version_no")
    private Integer versionNo;
}
