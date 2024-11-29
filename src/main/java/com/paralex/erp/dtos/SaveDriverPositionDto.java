package com.paralex.erp.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class SaveDriverPositionDto {
    @NotNull
    private double latitude;

    @NotNull
    private double longitude;

    @NotNull
    @NotEmpty
    @NotBlank
    private String driverProfileId;
}
