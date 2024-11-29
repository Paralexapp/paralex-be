package com.paralex.erp.controllers;

import com.paralex.erp.dtos.FindDriversNearbyDto;
import com.paralex.erp.entities.DriverProfileEntity;
import com.paralex.erp.services.DriverPositionService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@Tag(name = "Driver Position", description = "APIs to find drivers nearby...")
@SecurityRequirement(name = "Header Token")
@SecurityRequirement(name = "Cookie Token")
@RequiredArgsConstructor
@Validated
@CrossOrigin
@RestController
@RequestMapping("/service-provider/nearby/driver")
@Log4j2
public class DriverPositionController {
    private final DriverPositionService driverPositionService;

    public List<DriverProfileEntity> findDriversNearby(@NotNull FindDriversNearbyDto findDriversNearbyDto) {
        return driverPositionService.findDriversNearby(findDriversNearbyDto);
    }
}
