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
    List<Problem> findByContractId(String contractId);
    List<Problem> findByContractIdAndStatus(String contractId, ProblemStatus status);
    
    @Modifying
    @Query("DELETE FROM Problem p WHERE p.contractId = :contractId")
    void deleteByContractId(@Param("contractId") String contractId);
}
