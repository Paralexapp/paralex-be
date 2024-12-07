package com.paralex.erp.dtos;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DeliveryRequestDestinationDto {

    @NotBlank
    private String recipientName;

    @NotBlank
    private String phoneNumber;

    @NotBlank
    private String address;

    @NotNull
    private Double latitude; // Changed to wrapper type to ensure validation works

    @NotNull
    private Double longitude; // Changed to wrapper type to ensure validation works

    @NotBlank
    private String categoryId;

    @Nullable
    private String description;
}
