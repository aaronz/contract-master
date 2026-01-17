package com.contract.master.contract.domain.repository;

import com.contract.master.contract.domain.model.ContractAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContractAttachmentRepository extends JpaRepository<ContractAttachment, String> {
}
