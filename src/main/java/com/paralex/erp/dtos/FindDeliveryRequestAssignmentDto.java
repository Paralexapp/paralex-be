package com.paralex.erp.dtos;

import jakarta.annotation.Nullable;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class FindDeliveryRequestAssignmentDto extends DateTimePaginatedRequestDto {
    @Nullable
    private Boolean accepted;

    @Nullable
    private Boolean declined;

    @Nullable
    private String deliveryRequestId;

    @Nullable
    private String driverUserId;

    @Nullable
    private String driverProfileId;

    @Nullable
    private String creatorId;
}
