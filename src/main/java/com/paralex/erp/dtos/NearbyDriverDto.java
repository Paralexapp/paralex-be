package com.paralex.erp.dtos;

import com.paralex.erp.entities.DriverProfileEntity;
import com.paralex.erp.entities.UserEntity;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NearbyDriverDto {
    private String id;
    private UserEntity user; // User details of the driver
    private DriverProfileEntity driverProfile;
    private double distance; // Distance from the given location in kilometers
}
