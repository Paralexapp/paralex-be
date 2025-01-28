package com.paralex.erp.controllers;

import com.paralex.erp.configs.JwtService;
import com.paralex.erp.dtos.CreateAdminDto;
import com.paralex.erp.dtos.LoginDTO;
import com.paralex.erp.entities.AdminNotification;
import com.paralex.erp.entities.LawyerNotification;
import com.paralex.erp.entities.UserEntity;
import com.paralex.erp.repositories.AdminNotificationRepository;
import com.paralex.erp.services.AdminService;
import com.paralex.erp.services.NotificationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final NotificationService notificationService;
    private final AdminNotificationRepository adminNotificationRepository;
    private final JwtService jwtService;

    @PostMapping("/create-admin")
    public ResponseEntity<?> createAdmin(@NonNull @RequestBody CreateAdminDto admin) {

        try {


            Object response = adminService.createAdmin(admin);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }


    }

    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(exposedHeaders = "Authorization")
    public ResponseEntity<?> login(@NonNull @RequestBody LoginDTO dto) {
        try {
            String token = adminService.login(dto);
            // Create a response object to include the token in the body
            Map<String, String> responseBody = new HashMap<>();
            responseBody.put("token", token);

            return ResponseEntity.ok()
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .body(responseBody); // Set the body with token
        } catch (Exception e) {
            // Return the error message as JSON
                Map<String, String> errorBody = new HashMap<>();
            errorBody.put("error", e.getMessage());

            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorBody);
        }
    }


    @PostMapping("/create-test-admin-notification")
    public ResponseEntity<AdminNotification> createLawyerNotification(@RequestParam String title,
                                                                       @RequestParam String message,
                                                                       @RequestParam(required = false) String userId) {
        AdminNotification notification = new AdminNotification();
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

        AdminNotification savedNotification = adminNotificationRepository.save(notification);
        return ResponseEntity.ok(savedNotification);
    }

    // Get notifications for a lawyer
    @GetMapping("/get-admin-notification")
    public ResponseEntity<?> getAdminNotifications(
            @RequestParam(name = "userId", required = false) String userId,
            @RequestHeader("Authorization") String token) {
        try {
            // Validate the token and check if the user is an admin
            if (!adminService.isAdmin(token)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied. Only admins can access this resource.");
            }

            // Fetch notifications if the user is an admin
            List<AdminNotification> notifications = adminService.getAdminNotifications(userId);
            return ResponseEntity.ok(notifications);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token or authorization failed.");
        }
    }

}
