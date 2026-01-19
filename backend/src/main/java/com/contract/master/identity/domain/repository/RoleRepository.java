package com.contract.master.identity.domain.repository;

import com.contract.master.identity.domain.model.Role;
import com.contract.master.shared.domain.model.TenantId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    List<Role> findByTenantId(TenantId tenantId);
    java.util.Optional<Role> findByRoleIdAndTenantId(String roleId, TenantId tenantId);
}
