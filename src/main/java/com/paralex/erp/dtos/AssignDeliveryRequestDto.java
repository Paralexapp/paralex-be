package com.paralex.erp.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AssignDeliveryRequestDto {

    @NotNull(message = "Driver Profile ID is required")
    private String driverProfileId;

    @NotNull(message = "Delivery Request ID is required")
    private String deliveryRequestId;
}
