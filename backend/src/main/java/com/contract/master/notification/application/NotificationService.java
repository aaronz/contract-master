package com.contract.master.notification.application;

import com.contract.master.notification.domain.model.Notification;
import com.contract.master.notification.domain.repository.NotificationRepository;
import com.contract.master.audit.application.AuditService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private AuditService auditService;

    @Transactional
    public void resolveIssue(Long id) {
        notificationRepository.findById(id).ifPresent(n -> {
            n.setIsRead(true);
            n.setStatus("RESOLVED");
            notificationRepository.save(n);
            auditService.logChange(id.toString(), "notification", "OPEN", "RESOLVED", "MANUAL", "admin");
        });
    }

    @Transactional
    public void sendNotification(String userId, String title, String content, String type) {
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setTitle(title);
        notification.setContent(content);
        notification.setType(type);
        notification.setIsRead(false);
        
        notificationRepository.save(notification);
        log.info("Notification saved for user {}: {}", userId, title);
    }

    public List<Notification> getUserNotifications(String userId) {
        return notificationRepository.findByUserIdOrderByCreateTimeDesc(userId);
    }

    public void markAsRead(Long id) {
        notificationRepository.findById(id).ifPresent(n -> {
            n.setIsRead(true);
            notificationRepository.save(n);
        });
    }

    public void callbackCrm(String crmContractId, String status) {
        log.info("Callback to CRM for contract {}: status is {}", crmContractId, status);
    }
}
