package com.paralex.erp.services;

import com.paralex.erp.dtos.NotificationDTO;
import com.paralex.erp.entities.Notification;
import com.paralex.erp.repositories.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public void notifyUsers(String title, String message) {
        // Logic to send in-app notifications
        // This can be replaced with push notifications or WebSocket notifications
        System.out.println("Notification sent: " + title + " - " + message);
    }

    // Create a new notification
    public Notification createNotification(String title, String message, String userId) {
        Notification notification = new Notification();
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setUserId(userId); // Null for global notifications
        notification.setCreatedAt(LocalDateTime.now());
        notification.setRead(false);

        return notificationRepository.save(notification);
    }

    // Fetch notifications for a specific user
    public List<Notification> getUserNotifications(String userId) {
        return notificationRepository.findByUserId(userId);
    }

    // Mark notification as read
    public void markAsRead(String notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found with id: " + notificationId));
        notification.setRead(true);
        notificationRepository.save(notification);
    }

    public void broadcastNotification(String title, String message) {
        messagingTemplate.convertAndSend("/topic/notifications", new NotificationDTO(title, message));
    }
}
