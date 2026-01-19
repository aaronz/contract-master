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
    @Query("SELECT ej FROM EvaluationJob ej WHERE ej.tenantId.id = :id ORDER BY ej.createdAt DESC")
    List<EvaluationJob> findAllByTenantIdOrderByCreatedAtDesc(@org.springframework.data.repository.query.Param("id") String id);
    
    @Query("SELECT ej FROM EvaluationJob ej WHERE ej.tenantId.id = :tenantId")
    Page<EvaluationJob> findByTenantId(String tenantId, Pageable pageable);
    
    Optional<EvaluationJob> findByJobId(String jobId);

    @Query(value = "SELECT ej FROM EvaluationJob ej WHERE ej.tenantId.id = :id AND ej.targetContracts LIKE %:contractIdJson% AND ej.status = :status")
    List<EvaluationJob> findByTenantIdAndTargetContractsContainingAndStatus(@org.springframework.data.repository.query.Param("id") String id, String contractIdJson, JobStatus status);
}
