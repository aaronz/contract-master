package com.contract.master.problemcenter.domain.repository;

import com.contract.master.problemcenter.domain.model.ProblemEvaluationJob;
import com.contract.master.shared.domain.model.TenantId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProblemEvaluationJobRepository extends JpaRepository<ProblemEvaluationJob, Long> {
    List<ProblemEvaluationJob> findByContractIdAndTenantId(UUID contractId, TenantId tenantId);
}
