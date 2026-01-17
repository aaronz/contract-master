package com.contractmaster.evaluation.repository;

import com.contractmaster.evaluation.model.EvaluationJob;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EvaluationJobRepository extends JpaRepository<EvaluationJob, String> {
    List<EvaluationJob> findByTenantIdOrderByCreatedAtDesc(String tenantId);
    Page<EvaluationJob> findByTenantIdOrderByCreatedAtDesc(String tenantId, Pageable pageable);
}
