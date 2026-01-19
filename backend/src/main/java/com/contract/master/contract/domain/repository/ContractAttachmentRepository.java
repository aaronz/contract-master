package com.contract.master.contract.domain.repository;

import com.contract.master.contract.domain.model.ContractAttachment;
import com.contract.master.shared.domain.model.TenantId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ContractAttachmentRepository extends JpaRepository<ContractAttachment, Long> {
    List<ContractAttachment> findByContractId(String contractId);
    List<ContractAttachment> findByTenantId(TenantId tenantId);
    java.util.Optional<ContractAttachment> findByAttachmentId(String attachmentId);
}
