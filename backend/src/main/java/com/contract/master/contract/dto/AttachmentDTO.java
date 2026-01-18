package com.contract.master.contract.dto;

import java.time.LocalDateTime;
import java.util.Objects;

public class AttachmentDTO {
    private String attachmentId;
    private String attachmentName;
    private String attachmentType;
    private String fileFormat;
    private Long fileSize;
    private String uploadUser;
    private LocalDateTime uploadTime;

    public String getAttachmentId() {
        return attachmentId;
    }

    public void setAttachmentId(String attachmentId) {
        this.attachmentId = attachmentId;
    }

    public String getAttachmentName() {
        return attachmentName;
    }

    public void setAttachmentName(String attachmentName) {
        this.attachmentName = attachmentName;
    }

    public String getAttachmentType() {
        return attachmentType;
    }

    public void setAttachmentType(String attachmentType) {
        this.attachmentType = attachmentType;
    }

    public String getFileFormat() {
        return fileFormat;
    }

    public void setFileFormat(String fileFormat) {
        this.fileFormat = fileFormat;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getUploadUser() {
        return uploadUser;
    }

    public void setUploadUser(String uploadUser) {
        this.uploadUser = uploadUser;
    }

    public LocalDateTime getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(LocalDateTime uploadTime) {
        this.uploadTime = uploadTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AttachmentDTO that = (AttachmentDTO) o;
        return Objects.equals(attachmentId, that.attachmentId) && Objects.equals(attachmentName, that.attachmentName) && Objects.equals(attachmentType, that.attachmentType) && Objects.equals(fileFormat, that.fileFormat) && Objects.equals(fileSize, that.fileSize) && Objects.equals(uploadUser, that.uploadUser) && Objects.equals(uploadTime, that.uploadTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(attachmentId, attachmentName, attachmentType, fileFormat, fileSize, uploadUser, uploadTime);
    }
}
