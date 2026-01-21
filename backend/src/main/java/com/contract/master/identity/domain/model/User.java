package com.contract.master.identity.domain.model;

import com.contract.master.shared.domain.model.BaseTenantEntity;
import com.contract.master.shared.domain.model.TenantId;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Entity
@Table(name = "user_info")
public class User extends BaseTenantEntity implements UserDetails {

    @Column(name = "user_id", length = 64)
    private String userId;

    @Column(name = "user_name", length = 64)
    private String userName;

    @Column(name = "password", length = 128)
    private String password;

    @Column(name = "real_name", length = 64)
    private String realName;

    @Column(name = "status")
    private Integer status;

    public User() {
        
    }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    @Override
    public String getUsername() { return userName; }
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    @Override
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRealName() { return realName; }
    public void setRealName(String realName) { this.realName = realName; }

    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return status != null && status == 1; }
}
