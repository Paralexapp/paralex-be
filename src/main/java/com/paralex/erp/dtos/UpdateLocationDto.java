package com.paralex.erp.dtos;

import jakarta.annotation.Nullable;
import lombok.Data;

@Data
public class UpdateLocationDto {
    @Nullable
    private String name;

    @Nullable
    private Boolean status;

    @Nullable
    private Double latitude;

    @Nullable
    private Double longitude;
}
