package com.paralex.erp.services;

import com.paralex.erp.commons.utils.Helper;
import com.paralex.erp.configs.JwtService;
import com.paralex.erp.dtos.*;
import com.paralex.erp.entities.AdminNotification;
import com.paralex.erp.entities.NewWallet;
import com.paralex.erp.entities.UserEntity;
import com.paralex.erp.enums.RegistrationLevel;
import com.paralex.erp.enums.UserType;
import com.paralex.erp.exceptions.ErrorException;
import com.paralex.erp.exceptions.UserNotFoundException;
import com.paralex.erp.repositories.AdminNotificationRepository;
import com.paralex.erp.repositories.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminService {
    private static final Logger logger = LoggerFactory.getLogger(AdminService.class);
    private static final String INVALID_HEADER_RESPONSE = "Header is Empty or Invalid. Please retry with a valid one or contact the support ";
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AdminNotificationRepository adminNotificationRepository;
    private final Helper helper;

    public Object createAdmin(CreateAdminDto userDto) throws Exception {




            userRepository.findByEmail(userDto.getEmail()).ifPresentOrElse(
                    existingAdmin -> {


                        existingAdmin.setFirstName(userDto.getFirstName());
                        existingAdmin.setLastName(userDto.getLastName());
                        existingAdmin.setPhoneNumber(userDto.getPhoneNumber());

                    },
                    () -> {
                        UserEntity admin = new UserEntity();

                        admin.setEmail(userDto.getEmail());
                        admin.setFirstName(userDto.getFirstName());
                        admin.setLastName(userDto.getLastName());
                        admin.setPhoneNumber(userDto.getPhoneNumber());
                        admin.setTime(LocalDateTime.now());

                        admin.setPassword(helper.encodePassword(userDto.getPassword()));
                        admin.setUserType(UserType.ADMIN);
                        admin.setEnabled(true);

                        userRepository.save(admin);

                    }

            );

            return userDto;
    }

    public String login(LoginDTO dto) {



            // Retrieve the user from the repository based on email
            var user = userRepository.findByEmail(dto.getEmail())
                    .orElseThrow(() -> new UserNotFoundException("Invalid email or password"));

            // Validate the password
            if (!helper.encoder.matches(dto.getPassword(), user.getPassword())) {
                log.error("Invalid Password!");
                throw new RuntimeException("Invalid email or password!");
            }

            // Check if the user is an admin
            if (isAdmin(user)) {
                // Generate and return JWT Token
                try {
                    String token = jwtService.generateToken(user); // Ensure your jwt.generateToken method accepts a User object
                    return token;
                } catch (Exception e) {
                    log.error("Token generation failed", e);
                    throw new RuntimeException("Token generation failed");
                }
            } else {
                log.error("User is not an admin");
                throw new RuntimeException("User is not an admin");
            }

    }


    public boolean isAdmin(String token) {
        try {
            // Remove "Bearer " prefix if it exists
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }

            // Extract userId (email in your case) from the token
            String userId = jwtService.extractUsername(token);

            // Fetch the user by the extracted userId
            UserEntity user = userRepository.findByEmail(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Check if the user is an admin
            return isAdmin(user);
        } catch (Exception e) {
            throw new RuntimeException("Token validation failed or user is not an admin.");
        }
    }


    // Your existing isAdmin(UserEntity user) method
    protected boolean isAdmin(UserEntity user) {
        return UserType.ADMIN.equals(user.getUserType());
    }


    public List<String> getAllAdminEmailsByType(UserType userType) {
        return userRepository.findAllByUserType(userType)
                .stream()
                .map(UserEntity::getEmail)
                .toList();
    }

    public GlobalResponse<?> updateProfile(UpdateProfileDto updateProfileDto) throws Exception {
        UserEntity customer = userRepository.findByEmail(updateProfileDto.getEmail())
                .orElseThrow(() -> new ErrorException("Account not found"));

        customer.setFirstName(updateProfileDto.getFirstName());
        customer.setLastName(updateProfileDto.getLastName());
        customer.setPhoneNumber(updateProfileDto.getPhoneNumber());
        customer.setDateOfBirth(updateProfileDto.getDateOfBirth());
        customer.setName(updateProfileDto.getFirstName() + " " + updateProfileDto.getLastName());
        customer.setRegistrationLevel(RegistrationLevel.KYC_COMPLETED);
        userRepository.save(customer);

        // Return a response indicating the user profile update was successful
        GlobalResponse<String> response = new GlobalResponse<>();
        response.setStatus(HttpStatus.ACCEPTED);
        response.setMessage("User Profile Updated saved successfully");
        return response;
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

        // Initialize the map if it's null
        if (notification.getUserReadStatuses() == null) {
            notification.setUserReadStatuses(new HashMap<>());
        }

        // Put the userId and false as the read status
        if (userId != null) {
            notification.getUserReadStatuses().put(userId, false);
        }

        logger.info("Saving AdminNotification with userId: {}", userId);
        AdminNotification savedNotification = adminNotificationRepository.save(notification);
        logger.info("Saved Notification: {}", savedNotification);
        notification.setUserId(userId);
        adminNotificationRepository.save(notification);
        return savedNotification;
    }


    // Fetch notifications for a specific admin
    public List<AdminNotification> getAdminNotifications(String userId) {
        return adminNotificationRepository.findByUserId(userId);
    }

    public List<UserEntity> getAllUsers() {
        return userRepository.findByUserType(UserType.USER);
    }

    public List<UserEntity> getAllAdmins() {
        return userRepository.findByUserType(UserType.ADMIN);
    }
}
