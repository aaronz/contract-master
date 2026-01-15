package com.contract.master.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface DownstreamSystemRepository extends JpaRepository<DownstreamSystem, String> {
    List<DownstreamSystem> findByTenantId(String tenantId);
    Optional<DownstreamSystem> findByAccessKeyAndIsEnabledTrue(String accessKey);
}
