package com.contract.master.repository;

import com.contract.master.entity.ContractAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContractAttachmentRepository extends JpaRepository<ContractAttachment, String> {
}
