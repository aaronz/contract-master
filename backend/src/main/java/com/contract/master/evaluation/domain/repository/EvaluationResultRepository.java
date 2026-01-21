package com.contract.master.evaluation.domain.repository;

import com.contract.master.evaluation.domain.model.EvaluationResult;
import com.contract.master.shared.domain.model.TenantId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EvaluationResultRepository extends JpaRepository<EvaluationResult, Long> {
    List<EvaluationResult> findByJobId(String jobId);
}
