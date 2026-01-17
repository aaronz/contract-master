package com.contract.master.identity.domain.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "permission_info")
@Data
@EntityListeners(AuditingEntityListener.class)
public class Permission {
    @Id
    @Column(name = "perm_id", length = 64)
    private String permId;

    @Column(name = "perm_code", length = 64, unique = true)
    private String permCode;

    @Column(name = "perm_name", length = 64)
    private String permName;

    @Column(name = "perm_type", length = 32)
    private String permType;

    @CreatedDate
    @Column(name = "create_time", updatable = false)
    private LocalDateTime createTime;
}
