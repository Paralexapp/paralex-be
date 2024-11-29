package com.paralex.erp.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class DeliveryRequestAssignmentDto {
    @Id
    private String id;

    @NotNull
    private Boolean accepted;

    @NotNull
    private Boolean declined;

    @NotNull
    @NotEmpty
    @NotBlank
    private String deliveryRequestId;

    @NotNull
    @NotEmpty
    @NotBlank
    private String driverUserId;

    @NotNull
    @NotEmpty
    @NotBlank
    private String driverProfileId;

    @NotNull
    @NotEmpty
    @NotBlank
    private String creatorId;

    @NotNull
    private DeliveryRequestForAssignmentDto deliveryRequest;
}
