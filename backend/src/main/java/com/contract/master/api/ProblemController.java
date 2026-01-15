package com.contract.master.api;

import com.contract.master.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/problems")
public class ProblemController {

    @Autowired
    private NotificationService notificationService;

    @PostMapping("/{id}/resolve")
    public GlobalExceptionHandler.ApiResponse<Void> resolve(@PathVariable Long id) {
        notificationService.resolveIssue(id);
        return GlobalExceptionHandler.ApiResponse.success(null);
    }
}
