package com.contract.master.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContractBaseRepository extends JpaRepository<ContractBase, String> {
    Optional<ContractBase> findByContractNo(String contractNo);
    Optional<ContractBase> findByCrmId(String crmId);
}
