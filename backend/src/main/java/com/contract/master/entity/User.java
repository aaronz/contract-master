package com.contract.master.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_info")
@Data
@EntityListeners(AuditingEntityListener.class)
public class User extends BaseTenantEntity {
    @Id
    @Column(name = "user_id", length = 64)
    private String userId;

    @Column(name = "user_name", length = 64, unique = true)
    private String userName;

    @Column(name = "password", length = 128)
    private String password;

    @Column(name = "real_name", length = 64)
    private String realName;

    @Column(name = "email", length = 128)
    private String email;

    @Column(name = "user_type", length = 32)
    private String userType;

    @Column(name = "status")
    private Integer status;

    @CreatedDate
    @Column(name = "create_time", updatable = false)
    private LocalDateTime createTime;

    @LastModifiedDate
    @Column(name = "update_time")
    private LocalDateTime updateTime;
}
