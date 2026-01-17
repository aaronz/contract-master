package com.contract.master.evaluation.domain.repository;

import com.contract.master.evaluation.domain.model.EvaluationJob;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EvaluationJobRepository extends JpaRepository<EvaluationJob, String> {
    List<EvaluationJob> findAllByTenantIdOrderByCreatedAtDesc(String tenantId);
    Page<EvaluationJob> findByTenantId(String tenantId, Pageable pageable);

    // Custom query to find jobs where targetContracts JSON string contains the contractId
    @Query(value = "SELECT ej FROM EvaluationJob ej WHERE ej.tenantId = :tenantId AND ej.targetContracts LIKE %:contractIdJson% AND ej.status = :status")
    List<EvaluationJob> findByTenantIdAndTargetContractsContainingAndStatus(String tenantId, String contractIdJson, EvaluationJob.JobStatus status);
}
