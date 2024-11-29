package com.paralex.erp.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SubmitDeliveryRequestDto {
    @NotNull
    @NotEmpty
    @NotBlank
    private String driverProfileId;

    @NotNull
    private DeliveryRequestPickupDto pickup;

    @NotNull
    private DeliveryRequestDestinationDto destination;
}
