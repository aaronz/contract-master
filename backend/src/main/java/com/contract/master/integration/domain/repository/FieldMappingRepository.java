package com.contract.master.integration.domain.repository;

import com.contract.master.integration.domain.model.FieldMapping;
import com.contract.master.shared.domain.model.TenantId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FieldMappingRepository extends JpaRepository<FieldMapping, Long> {
}
