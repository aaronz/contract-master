package com.contractmaster.evaluation.repository;

import com.contractmaster.evaluation.model.EvaluationResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EvaluationResultRepository extends JpaRepository<EvaluationResult, String> {
    List<EvaluationResult> findByJobIdAndTenantId(String jobId, String tenantId);
}
