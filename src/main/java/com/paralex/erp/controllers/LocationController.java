package com.paralex.erp.controllers;

import com.paralex.erp.dtos.AddLocationDto;
import com.paralex.erp.dtos.PaginatedRequestDto;
import com.paralex.erp.dtos.UpdateItemDto;
import com.paralex.erp.entities.LocationEntity;
import com.paralex.erp.services.LocationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
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

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Tag(name = "Locations", description = "APIs to create, retrieve, disable locations e.g Iyana-Ipaja")
@SecurityRequirement(name = "Header Token")
@SecurityRequirement(name = "Cookie Token")
@RequiredArgsConstructor
@Validated
@CrossOrigin
@RestController
@RequestMapping("/locations")
@Log4j2
public class LocationController {
    private final LocationService locationService;

    @Operation(summary = "Create Location(s)",
            description = "Create a single or bulk of locations. It returns nothing.")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(
            value = "/",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, String> addLocations(@RequestBody @NotNull @NotEmpty List<AddLocationDto> addLocationDtoList) {
        locationService.addLocations(addLocationDtoList);
        return Collections.singletonMap("message", "Location added successfully");
    }


    @Operation(summary = "Update Location",
            description = "Make changes to attribute of this instance of locations")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping(
            value = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public String updateLocation(
            @PathVariable("id")
            @Parameter(name = "id", description = "A location instance ID", example = "f8c3de3d-1fea-4d7c-a8b0-29f63c4c3454")
            @NotNull @NotEmpty @NotBlank String id,
            @RequestBody @NotNull List<UpdateItemDto> changes) {
        locationService.updateLocation(id, changes);
        return  "Location updated successfully";
    }

    @Operation(summary = "Delete Location",
            description = "Totally delete an instance of a location")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public void deleteLocation(
            @PathVariable("id")
            @Parameter(name = "id", description = "A location instance ID", example = "f8c3de3d-1fea-4d7c-a8b0-29f63c4c3454")
            @NotNull String id) {
        locationService.deleteLocation(id);
    }

    @Operation(
            summary = "Get Locations",
            description = "Retrieve the list of locations for use in the litigation support axis.",
            parameters = {
                    @Parameter(name = "pageNumber", description = "Page number", required = false, schema = @Schema(defaultValue = "0")),
                    @Parameter(name = "pageSize", description = "Page size", required = false, schema = @Schema(defaultValue = "10"))
            }
    )
    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<LocationEntity> getLocations(
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize
    ) {
        PaginatedRequestDto paginatedRequestDto = new PaginatedRequestDto(pageNumber, pageSize);
        return locationService.getLocations(paginatedRequestDto);
    }

}
