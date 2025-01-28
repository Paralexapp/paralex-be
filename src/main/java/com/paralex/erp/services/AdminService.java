package com.paralex.erp.services;

import com.paralex.erp.commons.utils.Helper;
import com.paralex.erp.configs.JwtService;
import com.paralex.erp.dtos.CreateAdminDto;
import com.paralex.erp.dtos.LoginDTO;
import com.paralex.erp.entities.AdminNotification;
import com.paralex.erp.entities.UserEntity;
import com.paralex.erp.enums.UserType;
import com.paralex.erp.exceptions.UserNotFoundException;
import com.paralex.erp.repositories.AdminNotificationRepository;
import com.paralex.erp.repositories.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

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


    // Fetch notifications for a specific admin
    public List<AdminNotification> getAdminNotifications(String userId) {
        return adminNotificationRepository.findByUserId(userId);
    }
}
