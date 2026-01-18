package com.contract.master.problemcenter.domain.repository;

import com.contract.master.problemcenter.domain.model.Problem;
import com.contract.master.problemcenter.domain.model.ProblemStatus;
import com.contract.master.shared.domain.model.TenantId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProblemRepository extends JpaRepository<Problem, Long> {
    List<Problem> findByContractIdAndTenantId(UUID contractId, TenantId tenantId);
    List<Problem> findByContractIdAndStatusAndTenantId(UUID contractId, ProblemStatus status, TenantId tenantId);
}
