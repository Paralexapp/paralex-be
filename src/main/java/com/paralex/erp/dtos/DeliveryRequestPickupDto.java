package com.paralex.erp.dtos;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DeliveryRequestPickupDto {

    @NotBlank // Ensures non-null, non-empty, and no-whitespace-only strings
    private String customerName;

    @NotBlank
    private String phoneNumber;

    @NotBlank
    private String address;

    @NotNull // Ensures non-null for primitive types
    private Double latitude; // Changed to wrapper type to ensure validation works

    @NotNull
    private Double longitude; // Changed to wrapper type to ensure validation works

    @Nullable // Optional field
    private String description;
}
