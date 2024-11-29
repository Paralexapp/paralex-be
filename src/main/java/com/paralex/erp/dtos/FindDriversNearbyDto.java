package com.paralex.erp.dtos;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FindDriversNearbyDto {
    @Nullable
    private Double radius;

    @NotNull
    private double latitude;

    @NotNull
    private double longitude;
}
