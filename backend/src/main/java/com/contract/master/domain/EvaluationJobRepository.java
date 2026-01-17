package com.contract.master.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EvaluationJobRepository extends JpaRepository<EvaluationJob, String> {
    List<EvaluationJob> findByContractIdAndStatus(String contractId, String status);
    List<EvaluationJob> findByTenantId(String tenantId);
}