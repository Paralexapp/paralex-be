package com.paralex.erp.controllers;

import com.google.firebase.auth.FirebaseAuthException;
import com.paralex.erp.annotations.AuthorizationPolicy;
import com.paralex.erp.annotations.RequiredAuthorizationRecord;
import com.paralex.erp.dtos.*;
import com.paralex.erp.entities.DriverProfileEntity;
import com.paralex.erp.services.DriverProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Tag(name = "Driver Profiles", description = "APIs to create, retrieve, disable and enable Driver profiles")
@SecurityRequirement(name = "Header Token")
@SecurityRequirement(name = "Cookie Token")
@RequiredArgsConstructor
@Validated
@CrossOrigin
@RestController
@RequestMapping("/service-provider/driver/profile")
@Log4j2
public class DriverProfileController {
    private final DriverProfileService driverProfileService;

    @Operation(summary = "Driver Count",
            description = "Get the total count of drivers.")
    @AuthorizationPolicy(records = {
            @RequiredAuthorizationRecord(status = "Allow", resource = "UserCount", action = "Read"),
            @RequiredAuthorizationRecord(status = "Allow", resource = "PrincipalAdmin", action = "Read")})
    @GetMapping(
            value = "/count",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public TotalCountDto countDriverBy(CountDto countDto) {
        var total = driverProfileService.countAllOrBetweenTime(countDto);

        return TotalCountDto
                .builder()
                .total(total)
                .build();
    }

    @GetMapping(value = "/mine", produces = MediaType.APPLICATION_JSON_VALUE)
    public DriverProfileEntity getMyProfile() {
        return driverProfileService.getMyProfile();
    }

    @GetMapping(value = "/by", produces = MediaType.APPLICATION_JSON_VALUE)
    public DriverProfileEntity findDriverProfileBy(@NotNull FindDriverProfileDto findDriverProfileDto) {
        return driverProfileService.findDriverProfileBy(findDriverProfileDto);
    }

    @PutMapping(value = "/enable", produces = MediaType.APPLICATION_JSON_VALUE)
    public void enableProfile(@RequestBody @NotNull EnableProfileDto enableProfileDto) {
        driverProfileService.enableProfile(enableProfileDto);
    }

    @PutMapping(value = "/disable", produces = MediaType.APPLICATION_JSON_VALUE)
    public void disableProfile(@RequestBody @NotNull EnableProfileDto enableProfileDto) {
        driverProfileService.disableProfile(enableProfileDto);
    }

    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<DriverProfileEntity> getProfiles(@NotNull DateTimePaginatedRequestDto dateTimePaginatedRequestDto) throws IOException {
        return driverProfileService.getProfiles(dateTimePaginatedRequestDto);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(
            value = "/",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public GlobalResponse<?> createProfile(@RequestBody CreateDriverProfileDto createDriverProfileDto) throws Exception {
       return driverProfileService.createProfile(createDriverProfileDto);

    }

    @PatchMapping("/update-offline-status")
    public ResponseEntity<String> updateOfflineStatus(
            @RequestParam String driverId,
            @RequestParam boolean offlineStatus
    ) {
        return driverProfileService.updateOfflineStatus(driverId, offlineStatus)
                .map(driver -> ResponseEntity.ok("Offline status updated successfully."))
                .orElse(ResponseEntity.notFound().build());
    }



    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(
            value = "/my",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public GlobalResponse<?> createProfile(@RequestBody @NotNull CreateMyDriverProfileDto createMyDriverProfileDto) {
        return driverProfileService.createProfile(createMyDriverProfileDto);
    }

    @PostMapping("/rider/create-review")
    public ResponseEntity<?> reviewLawyer(@RequestParam String lawyerId, @RequestBody ReviewDto dto) {
        driverProfileService.submitReview(lawyerId, dto.getReviewerId(), dto.getRating(), dto.getComment());
        return ResponseEntity.ok(new GlobalResponse<>("Review submitted successfully", HttpStatus.OK));
    }

    @GetMapping("/rider/get-reviews")
    public ResponseEntity<?> getLawyerReviews(@RequestParam String driverId) {
        List<DriverReviewDTO> reviews = driverProfileService.getReviewsForRider(driverId);
        return ResponseEntity.ok(new GlobalResponse<>(reviews, HttpStatus.OK));
    }
}

