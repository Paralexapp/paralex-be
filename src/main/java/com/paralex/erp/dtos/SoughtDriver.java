package com.paralex.erp.dtos;

import com.paralex.erp.entities.DriverProfileEntity;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import java.time.LocalDateTime;

@Builder
@Data
public class SoughtDriver {
    @NotNull
    @NotEmpty
    @NotBlank
    private String id;

    @NotNull
    private GeoJsonPoint location;

    @NotNull
    @NotEmpty
    @NotBlank
    private String driverProfileId;

    @Nullable
    private DriverProfileEntity driverProfile;

    @NotNull
    private double calculatedDistance;

    @NotNull
    private LocalDateTime time;
}
