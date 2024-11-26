package com.paralex.erp.controllers;

import com.paralex.erp.dtos.*;
import com.paralex.erp.entities.UserEntity;
import com.paralex.erp.exceptions.UserNotFoundException;
import com.paralex.erp.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class MobileAuthController {

    private final UserService userService;

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
            "SERVICE_PROVIDER.")
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
    public ResponseEntity<String> uploadToCloudinary(@RequestParam("file") MultipartFile file, @RequestParam("email") String email) {
        try {
            String url = userService.uploadGeneralFile(file);
            return ResponseEntity.ok(url);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Failed to upload file: " + e.getMessage());
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
