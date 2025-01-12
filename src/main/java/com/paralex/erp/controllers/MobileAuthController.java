package com.paralex.erp.controllers;

import com.paralex.erp.dtos.*;
import com.paralex.erp.entities.UserEntity;
import com.paralex.erp.exceptions.UserNotFoundException;
import com.paralex.erp.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class MobileAuthController {

    private final UserService userService;

    private final Logger logError = LoggerFactory.getLogger(MobileAuthController.class);

    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public GlobalResponse<?> login(@RequestBody LoginDTO loginDTO) throws Exception{
        return  userService.login(loginDTO);
    }

    @Operation(summary = "GET THE LOGGED IN CUSTOMER INSTANCE .", description ="THIS CONTAINS ONLY COMMON ATTRIBUTES AMONGST EVERY " +
            "CUSTOMER INSTANCE IN THE APPLICATION")
    @GetMapping(value = "/get-user", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CustomerResponseDTO> getLoggedInUser(HttpServletRequest request){
        try {
            CustomerResponseDTO response = userService.getLoggedInUser(request);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);        }
    }

    @GetMapping(value = "get-user-by-id/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CustomerResponseDTO> getCustomerById(@PathVariable String id) {
        try {
            CustomerResponseDTO customerResponse = userService.getUserById(id);
            return new ResponseEntity<>(customerResponse, HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "get-user-by-email/email", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CustomerResponseDTO> getCustomerByEmail(@RequestParam String email) {
        Optional<UserEntity> customerOpt = userService.getUserByEmail(email);
        return getCustomerResponseDTOResponseEntity(customerOpt);
    }

    @GetMapping("/get-registration-level")
    public String getRegistrationLevel(HttpServletRequest request) {
        return userService.getRegistrationLevel(request);
    }

    @PostMapping(value = "logout", produces = MediaType.APPLICATION_JSON_VALUE)
    public GlobalResponse<?>   logout(){
        return userService.logout();
    }

    @Operation(summary = "APPLICATION ENTRY POINT .", description ="USERTYPES: USER OR " +
            "SERVICE_PROVIDER_LAWYER OR SERVICE_PROVIDER_RIDER")
    @PostMapping(value = "register", produces = MediaType.APPLICATION_JSON_VALUE)
    public GlobalResponse<?> register(@RequestBody RegistrationDto dto) {
        return userService.register(dto);
    }


    @Operation(summary = "Update User Profile .", description ="USERTYPES: USER OR " +
            "SERVICE_PROVIDER.")
    @PutMapping("/update-user-profile")
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

    @PostMapping("/upload-to-cloudinary")
    public ResponseEntity<String> uploadToCloudinary(@RequestParam("file") MultipartFile file) {
        try {
            String url = userService.uploadGeneralFile(file);
            return ResponseEntity.ok(url);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Failed to upload file: " + e.getMessage());
        }
    }

    @PostMapping("/upload-media")
    public ResponseEntity<Map<String, String>> uploadMedia(@RequestParam("file") MultipartFile file) {
        try {
            String secureUrl = userService.uploadAudio_Video(file);

            // Create a JSON response with the secure_url
            Map<String, String> response = new HashMap<>();
            response.put("secure_url", secureUrl);

            return ResponseEntity.ok(response);
        } catch (IOException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Error uploading file: " + e.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }


    @PostMapping(value = "initiate-password-reset", produces = MediaType.APPLICATION_JSON_VALUE)
    public GlobalResponse<?>  initiateRequest(@RequestParam String email) {
        return userService.initiatePasswordRequest(email);
    }

    @PostMapping(value = "reset-password", produces = MediaType.APPLICATION_JSON_VALUE)
    public GlobalResponse<?>  resetPassword(@RequestBody ForgotPasswordDto forgotPasswordDto) throws BadRequestException {
        return userService.forgotPassword(forgotPasswordDto);
    }

    @Operation(summary = "Reset password with an old, new and confirm password " +
            ". Password must contain at least 8 characters, a number, a special character and an uppercase letter.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Successfully reset password",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = inAppPasswordResetDTO.class)) }),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid reset parameters or conditions.",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized")})
    @PostMapping("/update-password/in-app")
    @ResponseBody
    public ResponseEntity<String> inAppResetPassword(@RequestBody inAppPasswordResetDTO dto, HttpServletRequest request) throws Exception {
        try {
            userService.inAppPasswordUpdate(dto, request);
            return new ResponseEntity<>( "Password reset successful!", HttpStatus.OK);
        } catch (Exception e) {
            logError.error("Error while resetting password: " + e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());

        }
    }

    @PostMapping(value = "validate-otp", produces = MediaType.APPLICATION_JSON_VALUE)
    public GlobalResponse<?>  validateOtp(@RequestBody ValidateDto otpDto){
        return userService.validateOtp(otpDto);
    }



    private ResponseEntity<CustomerResponseDTO> getCustomerResponseDTOResponseEntity(Optional<UserEntity> customerOpt) {
        if (customerOpt.isPresent()) {
            UserEntity customer = customerOpt.get();
            CustomerResponseDTO responseDTO = new ModelMapper().map(customer, CustomerResponseDTO.class);
            responseDTO.setId(customer.getId());
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
}
