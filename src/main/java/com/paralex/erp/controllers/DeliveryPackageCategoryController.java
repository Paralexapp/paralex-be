package com.paralex.erp.controllers;

import com.paralex.erp.documents.DeliveryPackageCategoryDocument;
import com.paralex.erp.dtos.AddDeliveryPackageCategoryDto;
import com.paralex.erp.dtos.FindDeliveryPackageCategoryDto;
import com.paralex.erp.dtos.UpdateItemDto;
import com.paralex.erp.services.DeliveryPackageCategoryService;
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

@Tag(name = "Delivery Package Categories", description = "APIs to create, retrieve, delivery package categories in destination.")
@SecurityRequirement(name = "Header Token")
@SecurityRequirement(name = "Cookie Token")
@RequiredArgsConstructor
@Validated
@CrossOrigin
@RestController
@RequestMapping("/delivery/package")
@Log4j2
public class DeliveryPackageCategoryController {
    private final DeliveryPackageCategoryService deliveryPackageCategoryService;

    @Operation(summary = "Create Delivery Stage(s)",
            description = "Create a single or bulk of delivery stages. It returns nothing.")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(
            value = "/",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public void addDeliveryPackageCategories(@RequestBody @NotNull @NotEmpty List<AddDeliveryPackageCategoryDto> addDeliveryPackageCategoryDtoList) {
        deliveryPackageCategoryService.addDeliveryPackageCategories(addDeliveryPackageCategoryDtoList);
    }

    @Operation(summary = "Get Delivery Package Categories",
            description = "Retrieve the list of delivery package categories.")
    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<DeliveryPackageCategoryDocument> findDeliveryPackageCategories(@NotNull FindDeliveryPackageCategoryDto findDeliveryPackageCategoryDto) {
        return deliveryPackageCategoryService.findDeliveryPackageCategories(findDeliveryPackageCategoryDto);
    }

    @Operation(summary = "Update Delivery Package Category",
            description = "Make changes to attribute of this instance of delivery package category")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping(
            value = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public void updateDeliveryPackageCategory(
            @PathVariable("id")
            @Parameter(name = "id", description = "A delivery package category instance ID", example = "f8c3de3d-1fea-4d7c-a8b0-29f63c4c3454")
            @NotNull @NotEmpty @NotBlank String id,
            @RequestBody @NotNull List<UpdateItemDto> changes) {
        deliveryPackageCategoryService.updateDeliveryPackageCategory(id, changes);
    }

    @Operation(summary = "Delete Delivery Package Category",
            description = "Totally delete an instance of a delivery package category")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public void deleteDeliveryPackageCategory(
            @PathVariable("id")
            @Parameter(name = "id", description = "A delivery package category instance ID", example = "f8c3de3d-1fea-4d7c-a8b0-29f63c4c3454")
            @NotNull String id) {
        deliveryPackageCategoryService.deleteDeliveryPackageCategory(id);
    }
}
