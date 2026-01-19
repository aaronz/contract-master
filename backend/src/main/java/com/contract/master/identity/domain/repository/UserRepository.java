package com.contract.master.identity.domain.repository;

import com.contract.master.identity.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserName(String userName);
    List<User> findByTenantId(com.contract.master.shared.domain.model.TenantId tenantId);
}
