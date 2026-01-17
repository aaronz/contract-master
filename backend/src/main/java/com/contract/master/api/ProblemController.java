package com.contract.master.api;

import com.contract.master.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*; // Import all annotations from rest

@RestController
@RequestMapping("/api/problems")
public class ProblemController {

    @Autowired
    private NotificationService notificationService;

    @PostMapping("/{id}/resolve")
    @PreAuthorize("hasRole('ADMIN')")
    public GlobalExceptionHandler.ApiResponse<Void> resolve(@PathVariable Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Invalid problem ID");
        }
        notificationService.resolveIssue(id);
        return GlobalExceptionHandler.ApiResponse.success(HttpStatus.OK, null);
    }
}
