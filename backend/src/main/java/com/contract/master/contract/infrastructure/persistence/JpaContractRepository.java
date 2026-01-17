package com.contract.master.contract.infrastructure.persistence;

import com.contract.master.contract.domain.model.Contract;
import com.contract.master.contract.domain.repository.ContractRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JpaContractRepository extends ContractRepository, JpaRepository<Contract, String> {
}
