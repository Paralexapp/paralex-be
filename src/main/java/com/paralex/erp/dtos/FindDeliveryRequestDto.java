package com.paralex.erp.dtos;

import jakarta.annotation.Nullable;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class FindDeliveryRequestDto extends DateTimePaginatedRequestDto {
    @Nullable
    private String trackingId;

    @Nullable
    private String deliveryStageId;

    @Nullable
    private String driverProfileId;

    @Nullable
    private String creatorId;
}
