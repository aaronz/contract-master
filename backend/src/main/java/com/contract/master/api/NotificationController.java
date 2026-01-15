package com.contract.master.api;

import com.contract.master.domain.Notification;
import com.contract.master.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping
    public List<Notification> list() {
        return notificationService.getUserNotifications("admin");
    }

    @PostMapping("/{id}/read")
    public void read(@PathVariable Long id) {
        notificationService.markAsRead(id);
    }
}
