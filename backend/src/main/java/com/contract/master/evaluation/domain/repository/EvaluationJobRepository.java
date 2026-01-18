package com.contract.master.evaluation.domain.repository;

import com.contract.master.evaluation.domain.model.EvaluationJob;
import com.contract.master.evaluation.domain.model.JobStatus;
import com.contract.master.shared.domain.model.TenantId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EvaluationJobRepository extends JpaRepository<EvaluationJob, Long> {
    List<EvaluationJob> findAllByTenantIdOrderByCreatedAtDesc(TenantId tenantId);
    Page<EvaluationJob> findByTenantId(TenantId tenantId, Pageable pageable);
    Optional<EvaluationJob> findByJobId(String jobId);

    @Query(value = "SELECT ej FROM EvaluationJob ej WHERE ej.tenantId = :tenantId AND ej.targetContracts LIKE %:contractIdJson% AND ej.status = :status")
    List<EvaluationJob> findByTenantIdAndTargetContractsContainingAndStatus(TenantId tenantId, String contractIdJson, EvaluationJob.JobStatus status);
}
