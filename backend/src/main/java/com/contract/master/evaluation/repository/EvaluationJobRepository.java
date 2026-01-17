package com.contract.master.evaluation.repository;

import com.contract.master.evaluation.model.EvaluationJob;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EvaluationJobRepository extends JpaRepository<EvaluationJob, String> {
    List<EvaluationJob> findAllByTenantIdOrderByCreatedAtDesc(String tenantId);
    Page<EvaluationJob> findByTenantId(String tenantId, Pageable pageable);
}
