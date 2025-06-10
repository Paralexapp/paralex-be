package com.paralex.erp.controllers;

import com.paralex.erp.dtos.*;
import com.paralex.erp.services.DeliveryRequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
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
import java.util.Map;

@Tag(name = "Delivery Request", description = "APIs to manage delivery requests and requests assignments for use by Admin and Drivers")
@SecurityRequirement(name = "Header Token")
@SecurityRequirement(name = "Cookie Token")
@RequiredArgsConstructor
@Validated
@CrossOrigin
@RestController
@RequestMapping("/delivery/request")
@Log4j2
public class DeliveryRequestController {
    private final DeliveryRequestService deliveryRequestService;

    @Operation(summary = "Get Delivery Request Information",
            description = "Retrieves the distance between the pickup and destination and the total amount.")
    @PostMapping(value = "/information", produces = MediaType.APPLICATION_JSON_VALUE)
    public DeliveryRequestInformationDto getDeliveryDistanceInformation(@RequestBody @Valid GetDeliveryRequestInformationDto getDeliveryRequestInformationDto) {
        return deliveryRequestService.getDeliveryDistanceInformation(getDeliveryRequestInformationDto);
    }


    @Operation(summary = "Track a Delivery Request",
            description = "Retrieve a delivery request by the Tracking ID, at all times it returns a list though it contains one element max.")
    @GetMapping(value = "/track/{trackingId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<DeliveryRequestDto> findDeliveryRequestByTrackingId(@NotNull @NotEmpty @NotBlank @PathVariable("trackingId") String trackingId) {
        return deliveryRequestService.findDeliveryRequestByTrackingId(trackingId);
    }

    @Operation(summary = "Decline Delivery Request Assignment",
            description = "Enables the driver to decline the delivery request assigned to them.")
    @PutMapping(value = "/assignment/decline", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> declineDeliveryRequestAssignment(@RequestBody @NotNull DeclineDeliveryRequestAssignmentDto declineDeliveryRequestAssignmentDto) {
        deliveryRequestService.declineDeliveryRequestAssignment(declineDeliveryRequestAssignmentDto);
        return ResponseEntity.ok(Map.of("message", "Delivery Request Accepted successfully"));}

    @Operation(summary = "Accept Delivery Request Assignment",
            description = "Enables the driver to accept the delivery request assigned to them.")
    @PutMapping(value = "/assignment/accept", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> acceptDeliveryRequestAssignment(@RequestBody @NotNull AcceptDeliveryRequestAssignmentDto acceptDeliveryRequestAssignmentDto) {
        deliveryRequestService.acceptDeliveryRequestAssignment(acceptDeliveryRequestAssignmentDto);
         return ResponseEntity.ok(Map.of("message", "Delivery Request Accepted successfully"));
    }

    @Operation(summary = "Get My Delivery Request Assignments",
            description = "Retrieve the list of delivery request assignments and related delivery request assigned to the user making the call.")
    @GetMapping(value = "/assignment/mine", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<DeliveryRequestAssignmentDto> getMyAssignedDeliveryRequests(@NotNull DateTimePaginatedRequestDto dateTimePaginatedRequestDto) {
        return deliveryRequestService.getMyAssignedDeliveryRequests(dateTimePaginatedRequestDto);
    }

    @Operation(summary = "Get Delivery Request Assignments",
            description = "Retrieve the list of delivery request assignments and related delivery request.")
    @GetMapping(value = "/assignment", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<DeliveryRequestAssignmentDto> getDeliveryRequestAssignments(@NotNull FindDeliveryRequestAssignmentDto findDeliveryRequestAssignmentDto) {
        return deliveryRequestService.getDeliveryRequestAssignments(findDeliveryRequestAssignmentDto);
    }

    @Operation(summary = "Assign Delivery Request to Rider",
            description = "Assign delivery request to a new Driver Profile.")
    @PostMapping("/assign")
    public ResponseEntity<Void> assignDeliveryRequest(@RequestBody @Valid AssignDeliveryRequestDto assignDeliveryRequestDto) {
        deliveryRequestService.assignDeliveryRequest(assignDeliveryRequestDto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Re-Assign Delivery Request",
            description = "Re-assign delivery request to a new Driver Profile.")
    @PostMapping(
            value = "/re-assign",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public void reAssignDeliveryRequest(@RequestBody @NotNull ReAssignDeliveryRequestDto reAssignDeliveryRequestDto) {
        deliveryRequestService.reAssignDeliveryRequest(reAssignDeliveryRequestDto);
    }

    @Operation(summary = "Change Delivery Request Stage",
            description = "Enables the driver to set the delivery request stage of a delivery request.")
    @PatchMapping(value = "/driver/stage", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public void setDeliveryRequestStageByDriver(@NotNull SetDeliveryRequestStageDto setDeliveryRequestStageDto) {
        deliveryRequestService.setDeliveryRequestStageByDriver(setDeliveryRequestStageDto);
    }

    @Operation(summary = "Change Delivery Request Stage",
            description = "Enables the admin to set the delivery request stage of a delivery request.")
    @PatchMapping(value = "/admin/stage", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public void setDeliveryRequestStageByAdmin(@RequestBody @NotNull SetDeliveryRequestStageDto setDeliveryRequestStageDto) {
        deliveryRequestService.setDeliveryRequestStageByAdmin(setDeliveryRequestStageDto);
    }

    @Operation(summary = "Submit Delivery Stage(s)",
            description = "Submit a delivery request. It returns some information such as tracking ID...")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(
            value = "/",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public SubmitDeliveryRequestResponseDto submitDeliveryRequest(@Valid @RequestBody SubmitDeliveryRequestDto submitDeliveryRequestDto) throws MessagingException, IOException {
        return deliveryRequestService.submitDeliveryRequest(submitDeliveryRequestDto);
    }

    @Operation(summary = "Get My Delivery Requests",
            description = "Retrieve the list of delivery requests for the user making the call.")
    @GetMapping(value = "/mine", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<DeliveryRequestDto> getDeliveryRequests(@NotNull PaginatedRequestDto paginatedRequestDto) {
        return deliveryRequestService.getMyDeliveryRequests(paginatedRequestDto);
    }

    @Operation(summary = "Get Delivery Requests",
            description = "Retrieve the list of delivery requests.")
    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<DeliveryRequestDto> getDeliveryRequests(@NotNull FindDeliveryRequestDto findDeliveryRequestDto) {
        return deliveryRequestService.getDeliveryRequests(findDeliveryRequestDto);
    }
}
