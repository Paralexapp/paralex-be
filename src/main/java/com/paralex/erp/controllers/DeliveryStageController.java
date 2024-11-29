package com.paralex.erp.controllers;

import com.paralex.erp.dtos.CreateDeliveryStageDto;
import com.paralex.erp.dtos.GetDeliveryStageDto;
import com.paralex.erp.dtos.PaginatedRequestDto;
import com.paralex.erp.dtos.UpdateItemDto;
import com.paralex.erp.entities.DeliveryStageDocument;
import com.paralex.erp.services.DeliveryStageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "Delivery Stages", description = "APIs to manage delivery stages for use by Admin and Drivers")
@SecurityRequirement(name = "Header Token")
@SecurityRequirement(name = "Cookie Token")
@RequiredArgsConstructor
@Validated
@CrossOrigin
@RestController
@RequestMapping("/delivery/stage")
@Log4j2
public class DeliveryStageController {
    private final DeliveryStageService deliveryStageService;

    @Operation(summary = "Delete Delivery Stage",
            description = "Totally delete an instance of a delivery stages")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public void deleteDeliveryStage(
            @PathVariable("id")
            @Parameter(name = "id", description = "A delivery stage instance ID", example = "f8c3de3d-1fea-4d7c-a8b0-29f63c4c3454")
            @NotNull String id) {
        deliveryStageService.deleteDeliveryStage(id);
    }

    @Operation(summary = "Update Delivery Stage",
            description = "Make changes to attribute of this instance of delivery stages")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping(
            value = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public void updateDeliveryStage(
            @PathVariable("id")
            @Parameter(name = "id", description = "A delivery stage instance ID", example = "f8c3de3d-1fea-4d7c-a8b0-29f63c4c3454")
            @NotNull @NotEmpty @NotBlank String id,
            @RequestBody @NotNull List<UpdateItemDto> changes) {
        deliveryStageService.updateDeliveryStage(id, changes);
    }

    @Operation(summary = "Get Delivery Stages (Driver)",
            description = "Retrieve the list of delivery stages meant for Drivers.")
    @GetMapping(value = "/driver", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<DeliveryStageDocument> getDeliveryStageForDrivers(@NotNull PaginatedRequestDto paginatedRequestDto) {
        return deliveryStageService.getDeliveryStageForDrivers(paginatedRequestDto);
    }

    @Operation(summary = "Get Delivery Stages",
            description = "Retrieve the list of delivery stages.")
    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<DeliveryStageDocument> getDeliveryStages(@NotNull GetDeliveryStageDto getDeliveryStageDto) {
        return deliveryStageService.getDeliveryStages(getDeliveryStageDto);
    }

    @Operation(summary = "Create Delivery Stage(s)",
            description = "Create a single or bulk of delivery stages. It returns nothing.")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(
            value = "/",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public void createDeliveryStages(@RequestBody @NotNull @NotEmpty List<CreateDeliveryStageDto> createDeliveryStageDtoList) {
        deliveryStageService.createDeliveryStages(createDeliveryStageDtoList);
    }
}
