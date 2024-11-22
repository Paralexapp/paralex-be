package com.paralex.erp.controllers;

import com.google.firebase.auth.FirebaseAuthException;
import com.paralex.erp.annotations.AuthorizationPolicy;
import com.paralex.erp.annotations.RequiredAuthorizationRecord;
import com.paralex.erp.dtos.*;
import com.paralex.erp.entities.UserEntity;
import com.paralex.erp.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Tag(name = "web-app-auth-controller", description = "User Onboarding, and Authentication companion APIs inclusive of Account Verification & Recovery")
@RequiredArgsConstructor
@Validated
@CrossOrigin
@RestController
@RequestMapping("/user")
@Log4j2
public class WebAppAuthController {
    private final UserService userService;

    @Operation(summary = "User Count",
            description = "Get the total count of users.")
    @SecurityRequirement(name = "Header Token")
    @SecurityRequirement(name = "Cookie Token")
    @AuthorizationPolicy(records = {
            @RequiredAuthorizationRecord(status = "Allow", resource = "UserCount", action = "Read"),
            @RequiredAuthorizationRecord(status = "Allow", resource = "PrincipalAdmin", action = "Read") })
    @GetMapping(
            value = "/count",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public TotalCountDto countUserBy(@RequestParam CountDto countDto) {
        var total = userService.countAllOrBetweenTime(countDto);

        return TotalCountDto
                .builder()
                .total(total)
                .build();
    }

    @Operation(summary = "Sign Out",
            description = "Sign User out of all apps, it returns a null cookie.")
    @SecurityRequirement(name = "Header Token")
    @SecurityRequirement(name = "Cookie Token")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping(
            value = "/sign-out",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> destroySession(@CookieValue("session") String token) throws IOException, FirebaseAuthException {
        userService.verifyIdToken(token, false);

        var headers = new HttpHeaders();

        headers.add(HttpHeaders.SET_COOKIE,
                userService.getDestroyedCookie()
                        .toString());

        return ResponseEntity.noContent()
                .headers(headers)
                .build();
    }

    @Operation(summary = "Create User Profile",
            description = "Create a user profile in the database.")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.TEXT_PLAIN_VALUE)
    public String createUserProfile(@RequestBody @NotNull CreateUserProfileDto createUserProfileDto) throws IOException, FirebaseAuthException {
        return userService.createUserProfile(createUserProfileDto);
    }

    @Operation(summary = "Find User Profile",
            description = "Find user profile by request payload.")
    @SecurityRequirement(name = "Header Token")
    @SecurityRequirement(name = "Cookie Token")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public UserEntity findUserBy(@RequestParam FindUserProfileDto findUserProfileDto) {
        return userService.findUserBy(findUserProfileDto);
    }

    @Operation(summary = "Verify User Profile",
            description = "Confirm user profile has been saved into database.")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping(
            value = "/verify",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserEntity> verifyUserProfile(@RequestBody @NotNull CreateUserProfileDto createUserProfileDto) throws IOException, FirebaseAuthException {
        var headers = new HttpHeaders();

        headers.add(HttpHeaders.SET_COOKIE,
                userService.getSignInCookie(createUserProfileDto.getIdToken())
                        .toString());

        return ResponseEntity
                .status(HttpStatus.OK)
                .headers(headers)
                .body(userService.verifyUserProfile(createUserProfileDto));
    }

    @Operation(summary = "Update User Details",
            description = "Modify the User instance attribute values at least for those that are modifiable.")
    @SecurityRequirement(name = "Header Token")
    @SecurityRequirement(name = "Cookie Token")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping(
            value = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public void updateUserDetails(
            @PathVariable("id")
            @Parameter(name = "id", description = "A User instance ID", example = "f8c3de3d-1fea-4d7c-a8b0-29f63c4c3454")
            @NotNull @NotEmpty @NotBlank String id,
            @RequestBody @NotNull List<UpdateItemDto> changes) {
        userService.updateUserDetails(id, changes);
    }
}
