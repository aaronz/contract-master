package com.contract.master.domain;

import com.contract.master.domain.ContractAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContractAttachmentRepository extends JpaRepository<ContractAttachment, String> {
}
