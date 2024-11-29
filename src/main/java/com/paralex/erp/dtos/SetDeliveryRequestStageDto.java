package com.paralex.erp.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SetDeliveryRequestStageDto {
    @NotNull
    @NotEmpty
    @NotBlank
    private String deliveryStageId;

    @NotNull
    @NotEmpty
    @NotBlank
    private String deliveryRequestAssignmentId;
}
