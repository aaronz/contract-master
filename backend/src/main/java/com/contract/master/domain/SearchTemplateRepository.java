package com.contract.master.domain;

import com.contract.master.domain.SearchTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SearchTemplateRepository extends JpaRepository<SearchTemplate, Long> {
    List<SearchTemplate> findByTenantIdAndUserId(String tenantId, String userId);
}
