package com.paralex.erp.controllers;

import com.google.firebase.auth.FirebaseAuthException;
import com.paralex.erp.dtos.CreateLawyerProfileDto;
import com.paralex.erp.dtos.DateTimePaginatedRequestDto;
import com.paralex.erp.dtos.EnableProfileDto;
import com.paralex.erp.services.LawyerProfileService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Tag(name = "Lawyer Profiles", description = "APIs to create, retrieve, disable and enable Laawyer profiles")
@SecurityRequirement(name = "Header Token")
@SecurityRequirement(name = "Cookie Token")
@RequiredArgsConstructor
@Validated
@CrossOrigin
@RestController
@RequestMapping("/service-provider/lawyer-profile")
@Log4j2
public class LawyerProfileController {
    private final LawyerProfileService lawyerProfileService;

    @PutMapping(value = "/enable", produces = MediaType.APPLICATION_JSON_VALUE)
    public void enableProfile(@RequestBody @NotNull EnableProfileDto enableProfileDto) {
        lawyerProfileService.enableProfile(enableProfileDto);
    }

    @PutMapping(value = "/disable", produces = MediaType.APPLICATION_JSON_VALUE)
    public void disableProfile(@RequestBody @NotNull EnableProfileDto enableProfileDto) {
        lawyerProfileService.disableProfile(enableProfileDto);
    }

    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public void getProfiles(@NotNull DateTimePaginatedRequestDto dateTimePaginatedRequestDto) {
        lawyerProfileService.getProfiles(dateTimePaginatedRequestDto);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(
            value = "/",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public void createProfile(@RequestBody @NotNull CreateLawyerProfileDto createLawyerProfileDto) throws IOException, FirebaseAuthException {
        lawyerProfileService.createProfile(createLawyerProfileDto);
    }
}
