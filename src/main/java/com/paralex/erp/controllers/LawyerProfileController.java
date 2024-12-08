package com.paralex.erp.controllers;

import com.google.firebase.auth.FirebaseAuthException;
import com.paralex.erp.annotations.AuthorizationPolicy;
import com.paralex.erp.annotations.RequiredAuthorizationRecord;
import com.paralex.erp.dtos.*;
import com.paralex.erp.entities.LawyerProfileEntity;
import com.paralex.erp.services.LawyerProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotEmpty;
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

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Tag(name = "Lawyer Profiles", description = "APIs to create, retrieve, disable and enable Lawyer profiles")
@SecurityRequirement(name = "Header Token")
@SecurityRequirement(name = "Cookie Token")
@RequiredArgsConstructor
@Validated
@CrossOrigin
@RestController
@RequestMapping("/service-provider/lawyer/profile")
@Log4j2
public class LawyerProfileController {
    private final LawyerProfileService lawyerProfileService;

    @Operation(summary = "Lawyer Count",
            description = "Get the total count of lawyers.")
    @SecurityRequirement(name = "Header Token")
    @SecurityRequirement(name = "Cookie Token")
    @AuthorizationPolicy(records = {
            @RequiredAuthorizationRecord(status = "Allow", resource = "UserCount", action = "Read"),
            @RequiredAuthorizationRecord(status = "Allow", resource = "PrincipalAdmin", action = "Read") })
    @GetMapping(
            value = "/count",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public TotalCountDto countDriverBy(CountDto countDto) {
        var total = lawyerProfileService.countAllOrBetweenTime(countDto);

        return TotalCountDto
                .builder()
                .total(total)
                .build();
    }

    @Operation(summary = "Search Lawyer Profiles",
            description = "Search for lawyer profiles around the provided location")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<LawyerProfileEntity> findLawyersAroundLocation(@NotNull FindLawyersNearLocation findLawyersNearLocation) {
        return lawyerProfileService.findLawyersAroundLocation(findLawyersNearLocation);
    }

    @Operation(summary = "Delete Lawyer Practice Area",
            description = "Totally delete an instance of a lawyer practice area")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/practice-area/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public void deleteLawyerPracticeArea(
            @PathVariable("id")
            @Parameter(name = "id", description = "A lawyer practice area mapping instance ID", example = "f8c3de3d-1fea-4d7c-a8b0-29f63c4c3454")
            @NotNull String id) {
        lawyerProfileService.deleteLawyerPracticeArea(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(
            value = "/practice-area",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public void addLawyerPracticeAreas(@RequestBody @NotNull @NotEmpty List<AddLawyerPracticeAreaDto> addLawyerPracticeAreaDtoList) {
        lawyerProfileService.addLawyerPracticeAreas(addLawyerPracticeAreaDtoList);
    }

    @GetMapping(value = "/mine", produces = MediaType.APPLICATION_JSON_VALUE)
    public LawyerProfileEntity findMyProfile() {
        return lawyerProfileService.findMyProfile();
    }

    @GetMapping(value = "/by", produces = MediaType.APPLICATION_JSON_VALUE)
    public LawyerProfileEntity findLawyerProfileBy(@NotNull FindLawyerProfileDto findLawyerProfileDto) {
        return lawyerProfileService.findLawyerProfileBy(findLawyerProfileDto);
    }

    @PutMapping(value = "/enable", produces = MediaType.APPLICATION_JSON_VALUE)
    public void enableProfile(@RequestBody @NotNull EnableProfileDto enableProfileDto) {
        lawyerProfileService.enableProfile(enableProfileDto);
    }

    @PutMapping(value = "/disable", produces = MediaType.APPLICATION_JSON_VALUE)
    public void disableProfile(@RequestBody @NotNull EnableProfileDto enableProfileDto) {
        lawyerProfileService.disableProfile(enableProfileDto);
    }

    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<LawyerProfileEntity> getProfiles(@NotNull DateTimePaginatedRequestDto dateTimePaginatedRequestDto) throws IOException {
        return lawyerProfileService.getProfiles(dateTimePaginatedRequestDto);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(
            value = "/",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public GlobalResponse<?> createProfile(@RequestBody CreateLawyerProfileDto createLawyerProfileDto) throws IOException, FirebaseAuthException {
        return lawyerProfileService.createProfile(createLawyerProfileDto);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(
            value = "/my",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public GlobalResponse<?> createProfile(@RequestBody @NotNull CreateMyLawyerProfileDto createMyLawyerProfileDto) {
        return lawyerProfileService.createProfile(createMyLawyerProfileDto);

    }
}
