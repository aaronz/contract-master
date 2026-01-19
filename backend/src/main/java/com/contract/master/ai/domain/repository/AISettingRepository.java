package com.contract.master.ai.domain.repository;

import com.contract.master.ai.domain.model.AISetting;
import com.contract.master.shared.domain.model.TenantId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface AISettingRepository extends JpaRepository<AISetting, Long> {
    Optional<AISetting> findByTenantId(TenantId tenantId);
}
