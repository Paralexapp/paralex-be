package com.paralex.erp.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SubmitDeliveryRequestDto {
//optional
//    private String driverProfileId;

    @NotNull // Ensures the object is not null
    @Valid // Triggers validation on nested object
    private DeliveryRequestPickupDto pickup;

    @NotNull // Ensures the object is not null
    @Valid // Triggers validation on nested object
    private DeliveryRequestDestinationDto destination;
}
