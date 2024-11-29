package com.paralex.erp.dtos;

import com.paralex.erp.entities.DeliveryStageDocument;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class DeliveryRequestDto {
    @Id
    private String id;

    @NotNull
    @NotEmpty
    @NotBlank
    private String trackingId;

    @NotNull
    private DeliveryStageDocument deliveryStage;

    @NotNull
    private List<DeliveryStageDocument> deliveryRequestAssignments = List.of();

    @NotNull
    @NotEmpty
    @NotBlank
    private String driverProfileId;

    @NotNull
    private DeliveryRequestPickupDto pickup;

    @NotNull
    private DeliveryRequestDestinationDto destination;

    @NotNull
    @NotEmpty
    @NotBlank
    private String creatorId;

    @NotNull
    private LocalDateTime time;
}
