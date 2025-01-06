package com.paralex.erp.controllers;

import com.paralex.erp.entities.Notification;
import com.paralex.erp.repositories.NotificationRepository;
import com.paralex.erp.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
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
            @RequestParam(name = "userId", required = false) String userId) {
        List<Notification> notifications = notificationService.getUserNotifications(userId);
        return ResponseEntity.ok(notifications);
    }


    // Mark a notification as read
    @PostMapping("/mark-as-read")
    public ResponseEntity<String> markAsRead(@RequestParam(name = "notificationId") String notificationId,
                                             @RequestParam(name = "userId") String userId) {
        String result = notificationService.markAsRead(notificationId, userId);
        return ResponseEntity.ok(result);
    }

    // Mark a notification as read
    @PostMapping("/inbox/mark-as-read")
    public ResponseEntity<String> markInboxAsRead(@RequestParam(name = "notificationId") String notificationId,
                                             @RequestParam(name = "userId") String userId) {
        String result = notificationService.markInboxAsRead(notificationId, userId);
        return ResponseEntity.ok(result);
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

        // Initialize the map if it's null
        if (notification.getUserReadStatuses() == null) {
            notification.setUserReadStatuses(new HashMap<>());
        }

        // Put the userId and false as the read status
        if (userId != null) {
            notification.getUserReadStatuses().put(userId, false);
        }

        Notification savedNotification = notificationRepository.save(notification);
        return ResponseEntity.ok(savedNotification);
    }

}
