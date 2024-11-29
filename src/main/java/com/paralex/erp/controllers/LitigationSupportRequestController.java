package com.paralex.erp.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.paralex.erp.dtos.*;
import com.paralex.erp.entities.AssignedLitigationSupportRequestEntity;
import com.paralex.erp.entities.LitigationSupportRequestEntity;
import com.paralex.erp.services.LitigationSupportRequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Tag(name = "Litigation Support Request", description = "APIs to submit litigation support requests")
@SecurityRequirement(name = "Header Token")
@SecurityRequirement(name = "Cookie Token")
@RequiredArgsConstructor
@Validated
@CrossOrigin
@RestController
@RequestMapping("/litigation-support")
@Log4j2
public class LitigationSupportRequestController {
    private final LitigationSupportRequestService litigationSupportRequestService;

    @Operation(summary = "Accept Negotiated Litigation Support Amount",
            description = "For customers & lawyers to accept the bargained price of the request.")
    @ResponseStatus(HttpStatus.OK)
    public void acceptNegotiationResolution(
            @PathVariable("id")
            @Parameter(name = "id", description = "A litigation support negotiation instance ID", example = "f8c3de3d-1fea-4d7c-a8b0-29f63c4c3454")
            @NotNull String id) {
        litigationSupportRequestService.acceptNegotiationResolution(id);
    }

    @Operation(summary = "Negotiate Litigation Support Request",
            description = "For customers & lawyers to negotiate the price of the request.")
    @ResponseStatus(HttpStatus.OK)
    public void negotiateCharges(
            @PathVariable("id")
            @Parameter(name = "id", description = "A litigation support request instance ID", example = "f8c3de3d-1fea-4d7c-a8b0-29f63c4c3454")
            @NotNull String id, @NotNull @RequestBody NegotiateLitigationSupportRequestDto negotiateLitigationSupportRequestDto) {
        litigationSupportRequestService.negotiateCharges(id, negotiateLitigationSupportRequestDto);
    }

    @Operation(summary = "Set File Number",
            description = "For lawyers to fill in file number from the court.")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = "/assigned/case-file-number", consumes = APPLICATION_JSON_VALUE)
    public void fillInFileNumber(
            @PathVariable("id")
            @Parameter(name = "id", description = "A litigation support request assignment instance ID", example = "f8c3de3d-1fea-4d7c-a8b0-29f63c4c3454") String id,
            @NotNull SetLitigationSupportFileNumberDto setLitigationSupportFileNumberDto) {
        litigationSupportRequestService.fillInFileNumber(id, setLitigationSupportFileNumberDto);
    }

    @Operation(summary = "Delete Law Firm",
            description = "Totally delete an instance of a law firm")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/file/{id}", produces = APPLICATION_JSON_VALUE)
    public void removeLitigationSupportRequestFile(
            @PathVariable("id")
            @Parameter(name = "id", description = "A law firm instance ID", example = "f8c3de3d-1fea-4d7c-a8b0-29f63c4c3454")
            @NotNull String id) {
        litigationSupportRequestService.removeLitigationSupportRequestFile(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(
            value = "/file/{id}",
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public void addLitigationSupportRequestFile(
            @PathVariable("id")
            @Parameter(name = "id", description = "A request file instance ID", example = "f8c3de3d-1fea-4d7c-a8b0-29f63c4c3454")
            @NotNull String id,
            @RequestBody @NotNull @NotEmpty List<AddFileDto> addFileDtoList) {
        litigationSupportRequestService.addLitigationSupportRequestFile(id, addFileDtoList);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping(
            value = "/assigned/re-assign/{id}",
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public void reassignLitigationSupportRequest(
            @PathVariable("id")
            @Parameter(name = "id", description = "A litigation support request instance ID", example = "f8c3de3d-1fea-4d7c-a8b0-29f63c4c3454")
            @NotNull String id,
            @RequestBody @NotNull ReAssignLitigationSupportRequestDto reAssignLitigationSupportRequestDto) {
        litigationSupportRequestService.reassignLitigationSupportRequest(id, reAssignLitigationSupportRequestDto);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping(
            value = "/assigned/reject/{id}",
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public void rejectLitigationSupportRequest(
            @PathVariable("id")
            @Parameter(name = "id", description = "A litigation support request instance ID", example = "f8c3de3d-1fea-4d7c-a8b0-29f63c4c3454")
            @NotNull String id) {
        litigationSupportRequestService
                .rejectLitigationSupportRequest(id);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping(
            value = "/assigned/accept/{id}",
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public void acceptLitigationSupportRequest(
            @PathVariable("id")
            @Parameter(name = "id", description = "A litigation support request instance ID", example = "f8c3de3d-1fea-4d7c-a8b0-29f63c4c3454")
            @NotNull String id, @NotNull @NotEmpty @RequestBody List<AddBillOfChargeDto> addBillOfChargeDtoList) throws JsonProcessingException {
        litigationSupportRequestService
                .acceptLitigationSupportRequest(id, addBillOfChargeDtoList);
    }

    @GetMapping(value = "/assigned/mine", produces = APPLICATION_JSON_VALUE)
    public List<AssignedLitigationSupportRequestEntity> getMyAssignedLitigationSupportRequests(@NotNull PaginatedRequestDto paginatedRequestDto) {
        return litigationSupportRequestService.getMyAssignedLitigationSupportRequests(paginatedRequestDto);
    }

    @GetMapping(value = "/assigned", produces = APPLICATION_JSON_VALUE)
    public List<AssignedLitigationSupportRequestEntity> getAssignedLitigationSupportRequests(@NotNull DateTimePaginatedRequestDto dateTimePaginatedRequestDto) {
        return litigationSupportRequestService.getAssignedLitigationSupportRequests(dateTimePaginatedRequestDto);
    }

    @GetMapping(value = "/mine", produces = APPLICATION_JSON_VALUE)
    public List<LitigationSupportRequestEntity> getMyLitigationSupportRequest(@NotNull PaginatedRequestDto paginatedRequestDto) {
        return litigationSupportRequestService.getMyLitigationSupportRequest(paginatedRequestDto);
    }

    @GetMapping(value = "/", produces = APPLICATION_JSON_VALUE)
    public List<LitigationSupportRequestEntity> getLitigationSupportRequest(@NotNull DateTimePaginatedRequestDto dateTimePaginatedRequestDto) {
        return litigationSupportRequestService.getLitigationSupportRequest(dateTimePaginatedRequestDto);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/", consumes = APPLICATION_JSON_VALUE)
    public void submitLitigationSupportRequest(@NotNull SubmitLitigationSupportRequestDto submitLitigationSupportRequestDto) {
        litigationSupportRequestService.submitLitigationSupportRequest(submitLitigationSupportRequestDto);
    }
}
