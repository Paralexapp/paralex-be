package com.paralex.erp.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class GetDeliveryRequestInformationDto {
    @NotNull
    private DeliveryRequestPickupDto pickup;

    @NotNull
    private DeliveryRequestDestinationDto destination;
}
