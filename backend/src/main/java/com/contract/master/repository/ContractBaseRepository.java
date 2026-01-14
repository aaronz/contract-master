package com.contract.master.repository;

import com.contract.master.entity.ContractBase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContractBaseRepository extends JpaRepository<ContractBase, String> {
    Optional<ContractBase> findByContractNo(String contractNo);
    Optional<ContractBase> findByCrmContractId(String crmContractId);
}
