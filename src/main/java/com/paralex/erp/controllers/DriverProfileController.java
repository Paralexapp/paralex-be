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
    public GlobalResponse<?> createProfile(@RequestBody @NotNull CreateDriverProfileDto createDriverProfileDto) throws Exception {
       return driverProfileService.createProfile(createDriverProfileDto);

    }


    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(
            value = "/my",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public GlobalResponse<?> createProfile(@RequestBody @NotNull CreateMyDriverProfileDto createMyDriverProfileDto) {
        return driverProfileService.createProfile(createMyDriverProfileDto);
    }
}

