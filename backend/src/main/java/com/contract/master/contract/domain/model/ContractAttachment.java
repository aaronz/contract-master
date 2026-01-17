package com.contract.master.contract.domain.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import com.contract.master.shared.domain.model.BaseTenantEntity;
import com.contract.master.shared.infrastructure.persistence.TenantEntityListener;

@Entity
@Table(name = "contract_attachment")
@Data
@EqualsAndHashCode(callSuper = true)
@EntityListeners({AuditingEntityListener.class, TenantEntityListener.class})
public class ContractAttachment extends BaseTenantEntity {
    @Id
    @Column(name = "attachment_id", length = 64)
    private String attachmentId;

    @Column(name = "contract_id", length = 64)
    private String contractId;

    @Column(name = "attachment_name", length = 255)
    private String attachmentName;

    @Column(name = "attachment_type", length = 32)
    private String attachmentType;

    @Column(name = "file_format", length = 16)
    private String fileFormat;

    @Column(name = "file_size")
    private Long fileSize;

    @Column(name = "storage_path", length = 512)
    private String storagePath;

    @Column(name = "is_main")
    private Boolean isMain;

    @Column(name = "upload_user", length = 64)
    private String uploadUser;

    @CreatedDate
    @Column(name = "upload_time", updatable = false)
    private LocalDateTime uploadTime;

    public void setAttachmentId(String attachmentId) { this.attachmentId = attachmentId; }
    public void setContractId(String contractId) { this.contractId = contractId; }
    public void setAttachmentName(String attachmentName) { this.attachmentName = attachmentName; }
    public void setStoragePath(String storagePath) { this.storagePath = storagePath; }
}
