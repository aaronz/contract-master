package com.contract.master.contract.interfaces.rest;

import com.contract.master.contract.domain.model.ContractAttachment;
import com.contract.master.contract.domain.repository.ContractAttachmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/attachments")
public class AttachmentController {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(AttachmentController.class);

    @Autowired
    private ContractAttachmentRepository attachmentRepository;

    @GetMapping("/view/{id}")
    public ResponseEntity<Resource> preview(@PathVariable String id) {
        return getFileResponse(id, false);
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> download(@PathVariable String id) {
        return getFileResponse(id, true);
    }

    private ResponseEntity<Resource> getFileResponse(String attachmentId, boolean isDownload) {
        return attachmentRepository.findByAttachmentId(attachmentId)
                .map(attachment -> {
                    File file = new File(attachment.getStoragePath());
                    if (!file.exists()) {
                        log.error("File not found on disk: {}", attachment.getStoragePath());
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).<Resource>build();
                    }

                    Resource resource = new FileSystemResource(file);
                    String contentType = attachment.getFileFormat();
                    if (contentType == null) contentType = "application/octet-stream";

                    ResponseEntity.BodyBuilder builder = ResponseEntity.ok()
                            .contentType(MediaType.parseMediaType(contentType));

                    if (isDownload) {
                        builder.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + attachment.getAttachmentName() + "\"");
                    } else {
                        builder.header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + attachment.getAttachmentName() + "\"");
                    }

                    return builder.body(resource);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
