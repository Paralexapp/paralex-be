package com.paralex.erp.controllers;

import com.paralex.erp.dtos.NearbyDriverDto;
import com.paralex.erp.services.DeliveryRequestService;
import com.paralex.erp.services.DriverProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class NearbyDriverController {


    private final DeliveryRequestService deliveryRequestService;
    private final DriverProfileService driverProfileService;

    /**
     * Get nearby drivers within a default or specified radius.
     * @param latitude - current user's latitude
     * @param longitude - current user's longitude
     * @param maxResults - max number of drivers to return (optional, defaults to 10)
     * @return list of nearby drivers (with distance)
     */
    @GetMapping("/nearby")
    public ResponseEntity<List<NearbyDriverDto>> getNearbyDrivers(
            @RequestParam double latitude,
            @RequestParam double longitude,
            @RequestParam(defaultValue = "10") int maxResults
    ) {
        List<NearbyDriverDto> nearbyDrivers = driverProfileService.findNearbyDrivers(latitude, longitude, maxResults);
        return ResponseEntity.ok(nearbyDrivers);
    }
}
