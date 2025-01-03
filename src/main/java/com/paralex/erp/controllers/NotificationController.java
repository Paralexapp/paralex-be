package com.paralex.erp.controllers;

import com.paralex.erp.entities.Notification;
import com.paralex.erp.repositories.NotificationRepository;
import com.paralex.erp.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;
    @Autowired
    private NotificationRepository notificationRepository;

    // Get notifications for a user
    @GetMapping("/get")
    public ResponseEntity<List<Notification>> getUserNotifications(
            @RequestParam(name = "userId", required = true) String userId) {
        return ResponseEntity.ok(notificationService.getUserNotifications(userId));
    }

    // Mark a notification as read
    @PostMapping("/read")
    public ResponseEntity<Void> markAsRead(@RequestParam(name = "notificationId", required = true) String notificationId) {
        notificationService.markAsRead(notificationId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/create-test-notification")
    public ResponseEntity<Notification> createNotification(@RequestParam String title,
                                                           @RequestParam String message,
                                                           @RequestParam(required = false) String userId) {
        Notification notification = new Notification();
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setUserId(userId); // Null for global notifications
        notification.setCreatedAt(LocalDateTime.now());
        notification.setRead(false);

        Notification savedNotification = notificationRepository.save(notification);
        return ResponseEntity.ok(savedNotification);
    }
}
