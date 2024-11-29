package com.paralex.erp.dtos;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DeliveryRequestPickupDto {
    @NotNull
    @NotEmpty
    @NotBlank
    private String customerName;

    @NotNull
    @NotEmpty
    @NotBlank
    private String phoneNumber;

    @NotNull
    @NotEmpty
    @NotBlank
    private String address;

    @NotNull
    private double latitude;

    @NotNull
    private double longitude;

    @Nullable
    private String description;
}
