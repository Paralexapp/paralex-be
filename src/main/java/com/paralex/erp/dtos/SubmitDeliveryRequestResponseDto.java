package com.paralex.erp.dtos;

import com.paralex.erp.entities.DeliveryStageDocument;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class SubmitDeliveryRequestResponseDto {
    @NotNull
    @NotEmpty
    @NotBlank
    private String id;

    @NotNull
    @NotEmpty
    @NotBlank
    private String trackingId;

    @NotNull
    private DeliveryStageDocument deliveryStage;
}
