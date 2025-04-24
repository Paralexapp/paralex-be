package com.paralex.erp.controllers;

import com.paralex.erp.configs.JwtService;
import com.paralex.erp.dtos.*;
import com.paralex.erp.entities.AdminNotification;
import com.paralex.erp.entities.BailBondEntity;
import com.paralex.erp.entities.LawyerProfileEntity;
import com.paralex.erp.entities.UserEntity;
import com.paralex.erp.repositories.AdminNotificationRepository;
import com.paralex.erp.services.*;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.constraints.NotNull;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
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
    private final BailBondService bailBondService;
    private final UserService userService;
    private final LawyerProfileService lawyerProfileService;

    @PostMapping(value = "/create-admin", produces = MediaType.APPLICATION_JSON_VALUE)
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

    @PostMapping(value = "/block", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> blockUser(@RequestParam String userId) {
        boolean blocked = userService.blockUser(userId);
        if (blocked) {
            return ResponseEntity.ok("User successfully blocked.");
        } else {
            return ResponseEntity.badRequest().body("User not found or already blocked.");
        }
    }

    @PostMapping(value = "/delete-user",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteUser(@RequestParam String userId) {
        boolean deleted = userService.deleteUser(userId);
        if (deleted) {
            return ResponseEntity.ok("User successfully deleted.");
        } else {
            return ResponseEntity.badRequest().body("User not found or already deleted.");
        }
    }


    @Operation(summary = "Update User Profile .", description ="USERTYPES: USER OR " +
            "SERVICE_PROVIDER.")
    @PutMapping(value = "/update-user-profile", produces = MediaType.APPLICATION_JSON_VALUE)
    public GlobalResponse<?> updateProfile(@RequestBody UpdateProfileDto updateProfileDto) {
        try {
            return userService.updateProfile(updateProfileDto);
        } catch (Exception ex) {
            GlobalResponse<String> errorResponse = new GlobalResponse<>();
            errorResponse.setStatus(HttpStatus.BAD_REQUEST);
            errorResponse.setMessage(ex.getMessage());
            return errorResponse;
        }
    }

    @PostMapping(value = "/create-test-admin-notification", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AdminNotification> createAdminNotification(@RequestParam String title,
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
    @GetMapping(value = "/get-admin-notification", produces = MediaType.APPLICATION_JSON_VALUE)
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

    @GetMapping(value = "/get-bail-bond-requests",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<BailBondEntity> getBailBondRequests(@NotNull PaginatedRequestDto paginatedRequestDto) {
        return bailBondService.getBailBondRequests(paginatedRequestDto);
    }

    @GetMapping(value = "/get-all-users", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserEntity>> getAllUsers() {
        return ResponseEntity.ok(adminService.getAllUsers());
    }

    @GetMapping(value = "/get-all-admins", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserEntity>> getAllAdmins() {
        return ResponseEntity.ok(adminService.getAllAdmins());
    }

    @GetMapping(value = "/get-all-lawyers-profile", produces = MediaType.APPLICATION_JSON_VALUE)
    public NewOkResponse<LawyerProfileEntity> getProfiles(
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize) throws IOException {

        PaginatedRequestDto paginatedRequestDto = new PaginatedRequestDto(pageNumber, pageSize);
        List<LawyerProfileEntity> lawyerProfiles = lawyerProfileService.getProfiles(paginatedRequestDto);

        return new NewOkResponse<>(
                HttpStatus.OK,
                "Success",
                "OK",
                lawyerProfiles
        );
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(
            value = "/create-lawyer-profile",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public GlobalResponse<?> createProfile(@RequestBody CreateLawyerProfileDto createLawyerProfileDto) throws Exception {
        return lawyerProfileService.adminCreateLawyerProfile(createLawyerProfileDto);
    }

}
