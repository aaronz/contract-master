package com.contract.master.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContractExtendDataRepository extends JpaRepository<ContractExtendData, Long> {
    List<ContractExtendData> findByContractId(String contractId);
}
