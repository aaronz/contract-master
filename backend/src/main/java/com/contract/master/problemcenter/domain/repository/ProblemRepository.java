package com.contract.master.problemcenter.domain.repository;

import com.contract.master.problemcenter.domain.model.Problem;
import com.contract.master.problemcenter.domain.model.ProblemStatus;
import com.contract.master.shared.domain.model.TenantId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface ProblemRepository extends JpaRepository<Problem, Long> {
    List<Problem> findByContractIdAndTenantId(String contractId, TenantId tenantId);
    List<Problem> findByContractIdAndStatusAndTenantId(String contractId, ProblemStatus status, TenantId tenantId);
    
    @Modifying
    @Query("DELETE FROM Problem p WHERE p.contractId = :contractId AND p.tenantId.id = :#{#tenantId.id}")
    void deleteByContractIdAndTenantId(@Param("contractId") String contractId, @Param("tenantId") TenantId tenantId);
}
