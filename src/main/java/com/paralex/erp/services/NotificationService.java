package com.paralex.erp.services;

import com.paralex.erp.dtos.NotificationDTO;
import com.paralex.erp.entities.AdminNotification;
import com.paralex.erp.entities.DriverNotification;
import com.paralex.erp.entities.LawyerNotification;
import com.paralex.erp.entities.Notification;
import com.paralex.erp.repositories.AdminNotificationRepository;
import com.paralex.erp.repositories.DriverNotificationRepository;
import com.paralex.erp.repositories.LawyerNotificationRepository;
import com.paralex.erp.repositories.NotificationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);


    private final NotificationRepository notificationRepository;
    @Autowired
    private DriverNotificationRepository driverNotificationRepository;
    @Autowired
    private LawyerNotificationRepository lawyerNotificationRepository;
    @Autowired
    private AdminNotificationRepository adminNotificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public void notifyUsers(String title, String message) {
        // Logic to send in-app notifications
        // This can be replaced with push notifications or WebSocket notifications
        System.out.println("Notification sent: " + title + " - " + message);
    }

    // Create a new user notification
    public Notification createNotification(String title, String message, String userId) {
        Notification notification = new Notification();
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setUserId(userId); // Null for global notifications
        notification.setCreatedAt(LocalDateTime.now());
        notification.setReadInbox(false);

        // Initialize the map if it's null
        if (notification.getUserReadStatuses() == null) {
            notification.setUserReadStatuses(new HashMap<>());
        }

        // Put the userId and false as the read status
        if (userId != null) {
            notification.getUserReadStatuses().put(userId, false);
        }

        return notificationRepository.save(notification);
    }

    // Create a new user notification
    public LawyerNotification createLawyerNotification(String title, String message, String userId) {
        LawyerNotification notification = new LawyerNotification();
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setUserId(userId); // Null for global notifications
        notification.setCreatedAt(LocalDateTime.now());
        notification.setReadInbox(false);

        // Initialize the map if it's null
        if (notification.getUserReadStatuses() == null) {
            notification.setUserReadStatuses(new HashMap<>());
        }

        // Put the userId and false as the read status
        if (userId != null) {
            notification.getUserReadStatuses().put(userId, false);
        }

        return lawyerNotificationRepository.save(notification);
    }

    // Create a new rider notification
    public DriverNotification createRiderNotification(String title, String message, String userId) {
        DriverNotification notification = new DriverNotification();
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setUserId(userId); // Null for global notifications
        notification.setCreatedAt(LocalDateTime.now());
        notification.setReadInbox(false);

        // Initialize the map if it's null
        if (notification.getUserReadStatuses() == null) {
            notification.setUserReadStatuses(new HashMap<>());
        }

        // Put the userId and false as the read status
        if (userId != null) {
            notification.getUserReadStatuses().put(userId, false);
        }

        return driverNotificationRepository.save(notification);
    }

    // Create a new admin notification
    public AdminNotification createAdminNotification(String title, String message, String userId) {
        logger.info("Creating AdminNotification with title: {} and message: {}", title, message);
        AdminNotification notification = new AdminNotification();
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setUserId(userId); // Null for global notifications
        notification.setCreatedAt(LocalDateTime.now());
        notification.setReadInbox(false);

//        // Initialize the map if it's null
//        if (notification.getUserReadStatuses() == null) {
//            notification.setUserReadStatuses(new HashMap<>());
//        }
//
//        // Put the userId and false as the read status
//        if (userId != null) {
//            notification.getUserReadStatuses().put(userId, false);
//        }

        logger.info("Saving AdminNotification with userId: {}", userId);
        AdminNotification savedNotification = adminNotificationRepository.save(notification);
        logger.info("Saved Notification: {}", savedNotification);
notification.setUserId(userId);
adminNotificationRepository.save(notification);
        return savedNotification;
    }

    // Fetch notifications for a specific user
    public List<Notification> getUserNotifications(String userId) {
        return notificationRepository.findByUserId(userId);
    }


    // Fetch notifications for a specific rider
    public List<DriverNotification> getRiderNotifications(String userId) {
        return driverNotificationRepository.findByUserId(userId);
    }

    // Fetch notifications for a specific lawyer
    public List<LawyerNotification> getLawyerNotifications(String userId) {
        return lawyerNotificationRepository.findByUserId(userId);
    }

    public String markAsRead(String notificationId, String userId) {
        // Retrieve the notification by ID
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found with id: " + notificationId));

        // If it's a global notification (userId is null), or if the notification is targeted at this specific user
//        if (notification.getUserId() == null || notification.getUserId().equals(userId))
//        {
            // Mark the notification as read by the user (set to true in the map)
            notification.getUserReadStatuses().put(userId, true);
//        }

        // Save the updated notification
        notificationRepository.save(notification);
        return "Notification marked as read successfully";
    }

    public String markInboxAsRead(String notificationId, String userId) {
        // Retrieve the notification by ID
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found with id: " + notificationId));

        //if the notification is targeted at this specific user
        if (notification.getUserId().equals(userId))
        {
        // Mark the inbox notification as read by the user (set to true in the map)
        notification.setReadInbox(true);
        }

        // Save the updated notification
        notificationRepository.save(notification);
        return "Notification marked as read successfully";
    }

    public String markAsReadLawyer(String notificationId, String userId) {
        // Retrieve the notification by ID
        LawyerNotification notification = lawyerNotificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found with id: " + notificationId));

        // If it's a global notification (userId is null), or if the notification is targeted at this specific user
//        if (notification.getUserId() == null || notification.getUserId().equals(userId))
//        {
        // Mark the notification as read by the user (set to true in the map)
        notification.getUserReadStatuses().put(userId, true);
//        }

        // Save the updated notification
        lawyerNotificationRepository.save(notification);
        return "Notification marked as read successfully";
    }

    public String markLawyerInboxAsRead(String notificationId, String userId) {
        // Retrieve the notification by ID
        LawyerNotification notification = lawyerNotificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found with id: " + notificationId));

        //if the notification is targeted at this specific user
        if (notification.getUserId().equals(userId))
        {
            // Mark the inbox notification as read by the user (set to true in the map)
            notification.setReadInbox(true);
        }

        // Save the updated notification
        lawyerNotificationRepository.save(notification);
        return "Notification marked as read successfully";
    }

    public String markAsReadRider(String notificationId, String userId) {
        // Retrieve the notification by ID
        DriverNotification notification = driverNotificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found with id: " + notificationId));

        // If it's a global notification (userId is null), or if the notification is targeted at this specific user
//        if (notification.getUserId() == null || notification.getUserId().equals(userId))
//        {
        // Mark the notification as read by the user (set to true in the map)
        notification.getUserReadStatuses().put(userId, true);
//        }

        // Save the updated notification
        driverNotificationRepository.save(notification);
        return "Notification marked as read successfully";
    }

    public String markRiderInboxAsRead(String notificationId, String userId) {
        // Retrieve the notification by ID
        DriverNotification notification = driverNotificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found with id: " + notificationId));

        //if the notification is targeted at this specific user
        if (notification.getUserId().equals(userId))
        {
            // Mark the inbox notification as read by the user (set to true in the map)
            notification.setReadInbox(true);
        }

        // Save the updated notification
        driverNotificationRepository.save(notification);
        return "Notification marked as read successfully";
    }

    public void broadcastNotification(String title, String message) {
        messagingTemplate.convertAndSend("/topic/notifications", new NotificationDTO(title, message));
    }
}
