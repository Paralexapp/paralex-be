package com.paralex.erp.controllers;

import com.paralex.erp.annotations.AuthorizationPolicy;
import com.paralex.erp.annotations.RequiredAuthorizationRecord;
import com.paralex.erp.documents.AuthorizationRecordDocument;
import com.paralex.erp.dtos.AddAuthorizationRecordDto;
import com.paralex.erp.dtos.FindAuthorizationRecordDto;
import com.paralex.erp.dtos.GetEvaluationRecordDto;
import com.paralex.erp.services.AuthorizationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Authorization (Privilege Based Access-Control)", description = "APIs required to give users privileges and retrieve authorization records all or for evaluation")
@SecurityRequirement(name = "Header Token")
@SecurityRequirement(name = "Cookie Token")
@RequiredArgsConstructor
@Validated
@CrossOrigin
@RestController
@RequestMapping("/authorization")
@Log4j2
public class AuthorizationController {
    private final AuthorizationService authorizationService;

    @Operation(summary = "Get evaluation records",
            description = "Get authorization records to use in evaluation principal access")
    @PutMapping(value = "/record-evaluation", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<AuthorizationRecordDocument> getEvaluationRecords(
            @Valid @RequestBody @NotNull List<GetEvaluationRecordDto> getEvaluationRecordDtoList) {
        return authorizationService.getEvaluationRecords(getEvaluationRecordDtoList);
    }

    @Operation(summary = "Get all authorization records",
            description = "A list of all for enumeration purposes, does not filter by multiple criteria")
    @AuthorizationPolicy(
            records = {
                    @RequiredAuthorizationRecord(status = "Allow", resource = "Authorization", action = "Read"),
                    @RequiredAuthorizationRecord(status = "Allow", resource = "PrincipalAdmin", action = "Read") })
    @GetMapping(value = "/record", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<AuthorizationRecordDocument> getAuthorizationRecords(@Valid FindAuthorizationRecordDto findAuthorizationRecordDto) {
        return authorizationService.getAuthorizationRecords(findAuthorizationRecordDto);
    }

    @Operation(summary = "Add Authorization Records",
            description = "Save authorization records against user given the body provided")
    @AuthorizationPolicy(
            records = {
                    @RequiredAuthorizationRecord(status = "Allow", resource = "Authorization", action = "Create"),
                    @RequiredAuthorizationRecord(status = "Allow", resource = "PrincipalAdmin", action = "Read") })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(
            value = "/record",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public void addAuthorizationRecord(@RequestBody @NotNull List<AddAuthorizationRecordDto> authorizationRecordDtoList) {
        authorizationService.addAuthorizationRecord(authorizationRecordDtoList);
    }

    @Operation(summary = "Deny Authorization Records",
            description = "Turn Status off on authorization record")
    @AuthorizationPolicy(
            records = {
                    @RequiredAuthorizationRecord(status = "Allow", resource = "Authorization", action = "Update"),
                    @RequiredAuthorizationRecord(status = "Allow", resource = "PrincipalAdmin", action = "Read") })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping(
            value = "/record/deny/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public void denyPrincipalForAuthorizationRecord(
            @PathVariable("id")
            @Parameter(name = "id", description = "An Authorization Record instance ID", example = "00000020f51bb4362eee2a4d")
            @NotNull String id) {
        authorizationService.denyPrincipalForAuthorizationRecord(id);
    }

    // pathVariablePattern = "/authorization/record/allow/{id}",
    @Operation(summary = "Allow Authorization Records",
            description = "Turn Status on on authorization record")
    @AuthorizationPolicy(
            records = {
                    @RequiredAuthorizationRecord(status = "Allow", resource = "Authorization", action = "Update"),
                    @RequiredAuthorizationRecord(status = "Allow", resource = "PrincipalAdmin", action = "Read") })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping(
            value = "/record/allow/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public void allowPrincipalForAuthorizationRecord(
            @PathVariable("id")
            @Parameter(name = "id", description = "An Authorization Record instance ID", example = "00000020f51bb4362eee2a4d")
            @NotNull String id) {
        authorizationService.allowPrincipalForAuthorizationRecord(id);
    }

    @Operation(summary = "Remove Authorization Records",
            description = "Completely delete the authorization record instance")
    @AuthorizationPolicy(
            records = {
                    @RequiredAuthorizationRecord(status = "Allow", resource = "Authorization", action = "Delete"),
                    @RequiredAuthorizationRecord(status = "Allow", resource = "PrincipalAdmin", action = "Read") })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/record/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public void removeAuthorizationRecordBy(
            @PathVariable("id")
            @Parameter(name = "id", description = "An Authorization Record instance ID", example = "00000020f51bb4362eee2a4d")
            @NotNull @NotEmpty @NotBlank String id) {
        authorizationService.removeAuthorizationRecordBy(id);
    }
}
