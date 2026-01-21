package com.contract.master.contract.domain.repository;

import com.contract.master.contract.domain.model.SearchTemplate;
import com.contract.master.shared.domain.model.TenantId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SearchTemplateRepository extends JpaRepository<SearchTemplate, Long> {
    List<SearchTemplate> findByUserId(String userId);
}
