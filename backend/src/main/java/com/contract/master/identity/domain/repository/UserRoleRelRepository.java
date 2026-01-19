package com.contract.master.identity.domain.repository;

import com.contract.master.identity.domain.model.UserRoleRel;
import com.contract.master.shared.domain.model.TenantId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UserRoleRelRepository extends JpaRepository<UserRoleRel, Long> {
    List<UserRoleRel> findByUserIdAndTenantId(String userId, TenantId tenantId);
    void deleteByUserIdAndTenantId(String userId, TenantId tenantId);
}
