package com.contract.master.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "search_template")
@Data
@EqualsAndHashCode(callSuper = true)
public class SearchTemplate extends BaseTenantEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "template_name", length = 128)
    private String templateName;

    @Column(name = "user_id", length = 64)
    private String userId;

    @Column(name = "filter_json", columnDefinition = "TEXT")
    private String filterJson;
}
