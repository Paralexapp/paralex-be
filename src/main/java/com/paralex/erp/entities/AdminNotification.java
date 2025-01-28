package com.paralex.erp.entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Setter
@Getter
@Document(collection = "adminNotifications")
public class AdminNotification {

    @Id
    private String id;
    private String title;
    private String message;
    private String userId=null;// Optional: Targeted user ID, null if it's for all users
    private LocalDateTime createdAt;
    private boolean readInbox;
    private boolean globalRead = false; // Default: not read globally
    private Map<String, Boolean> userReadStatuses = new HashMap<>(); // Tracks each user's read status


}
