package com.contract.master.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface WebHookConfigRepository extends JpaRepository<WebHookConfig, Long> {
    List<WebHookConfig> findByTenantId(String tenantId);
}
