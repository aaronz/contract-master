package com.contract.master.notification.interfaces.rest;

import com.contract.master.notification.domain.model.Notification;
import com.contract.master.notification.application.NotificationService;
import com.contract.master.api.GlobalExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/my")
    public GlobalExceptionHandler.ApiResponse<List<Notification>> list() {
        return GlobalExceptionHandler.ApiResponse.success(HttpStatus.OK, notificationService.getUserNotifications("admin"));
    }

    @PostMapping("/{id}/read")
    public GlobalExceptionHandler.ApiResponse<Void> read(@PathVariable Long id) {
        notificationService.markAsRead(id);
        return GlobalExceptionHandler.ApiResponse.success(HttpStatus.OK, null);
    }
}
