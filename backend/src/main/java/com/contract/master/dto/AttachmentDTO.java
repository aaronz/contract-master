package com.contract.master.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AttachmentDTO {
    private String attachmentId;
    private String attachmentName;
    private String attachmentType;
    private String fileFormat;
    private Long fileSize;
    private String uploadUser;
    private LocalDateTime uploadTime;
}
