package com.paralex.erp.controllers;

import com.paralex.erp.entities.DriverNotification;
import com.paralex.erp.entities.LawyerNotification;
import com.paralex.erp.entities.Notification;
import com.paralex.erp.repositories.DriverNotificationRepository;
import com.paralex.erp.repositories.LawyerNotificationRepository;
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
    @Autowired
    private LawyerNotificationRepository lawyerNotificationRepository;
    @Autowired
    private DriverNotificationRepository driverNotificationRepository;

    // Get notifications for a user
    @GetMapping("/get")
    public ResponseEntity<List<Notification>> getUserNotifications(
            @RequestParam(name = "userId", required = false) String userId) {
        List<Notification> notifications = notificationService.getUserNotifications(userId);
        return ResponseEntity.ok(notifications);
    }

    // Get notifications for a lawyer
    @GetMapping("/get-lawyer-notification")
    public ResponseEntity<List<LawyerNotification>> getLawyerNotifications(
            @RequestParam(name = "userId", required = false) String userId) {
        List<LawyerNotification> notifications = notificationService.getLawyerNotifications(userId);
        return ResponseEntity.ok(notifications);
    }

    // Get notifications for a rider
    @GetMapping("/get-rider-notification")
    public ResponseEntity<List<DriverNotification>> getDriverNotifications(
            @RequestParam(name = "userId", required = false) String userId) {
        List<DriverNotification> notifications = notificationService.getRiderNotifications(userId);
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

    // Mark a notification as read
    @PostMapping("/mark-as-read-lawyer")
    public ResponseEntity<String> markAsReadLawyer(@RequestParam(name = "notificationId") String notificationId,
                                             @RequestParam(name = "userId") String userId) {
        String result = notificationService.markAsReadLawyer(notificationId, userId);
        return ResponseEntity.ok(result);
    }

    // Mark a notification as read
    @PostMapping("/inbox/lawyer/mark-as-read")
    public ResponseEntity<String> markLawyerInboxAsRead(@RequestParam(name = "notificationId") String notificationId,
                                                  @RequestParam(name = "userId") String userId) {
        String result = notificationService.markLawyerInboxAsRead(notificationId, userId);
        return ResponseEntity.ok(result);
    }

    // Mark a notification as read
    @PostMapping("/mark-as-read-rider")
    public ResponseEntity<String> markAsReadRider(@RequestParam(name = "notificationId") String notificationId,
                                             @RequestParam(name = "userId") String userId) {
        String result = notificationService.markAsReadRider(notificationId, userId);
        return ResponseEntity.ok(result);
    }

    // Mark a notification as read
    @PostMapping("/inbox/rider/mark-as-read")
    public ResponseEntity<String> markRiderInboxAsRead(@RequestParam(name = "notificationId") String notificationId,
                                                  @RequestParam(name = "userId") String userId) {
        String result = notificationService.markRiderInboxAsRead(notificationId, userId);
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

    @PostMapping("/create-test-lawyer-notification")
    public ResponseEntity<LawyerNotification> createLawyerNotification(@RequestParam String title,
                                                           @RequestParam String message,
                                                           @RequestParam(required = false) String userId) {
        LawyerNotification notification = new LawyerNotification();
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

        LawyerNotification savedNotification = lawyerNotificationRepository.save(notification);
        return ResponseEntity.ok(savedNotification);
    }

    @PostMapping("/create-test-rider-notification")
    public ResponseEntity<DriverNotification> createRiderNotification(@RequestParam String title,
                                                           @RequestParam String message,
                                                           @RequestParam(required = false) String userId) {
        DriverNotification notification = new DriverNotification();
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

        DriverNotification savedNotification = driverNotificationRepository.save(notification);
        return ResponseEntity.ok(savedNotification);
    }


}
