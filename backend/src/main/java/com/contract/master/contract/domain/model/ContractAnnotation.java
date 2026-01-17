package com.contract.master.contract.domain.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.contract.master.shared.domain.model.BaseTenantEntity;

@Entity
@Table(name = "contract_annotation")
@Data
@EqualsAndHashCode(callSuper = true)
public class ContractAnnotation extends BaseTenantEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "contract_id", length = 64)
    private String contractId;

    @Column(name = "user_id", length = 64)
    private String userId;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "status", length = 32)
    private String status;
}
