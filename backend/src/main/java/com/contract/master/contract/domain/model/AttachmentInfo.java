package com.contract.master.contract.domain.model;

import com.contract.master.shared.domain.base.ValueObject;
import java.util.Objects;
import java.util.UUID;

public record AttachmentInfo(
        UUID attachmentId,
        String fileName,
        String fileType,
        String storagePath,
        boolean isMain) implements ValueObject {
    public AttachmentInfo {
        Objects.requireNonNull(attachmentId, "Attachment ID cannot be null");
        Objects.requireNonNull(fileName, "File name cannot be null");
    }
}
