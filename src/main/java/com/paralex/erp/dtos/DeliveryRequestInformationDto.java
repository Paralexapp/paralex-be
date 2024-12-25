package com.paralex.erp.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class DeliveryRequestInformationDto {
    @NotNull
    private int amount;

    @NotNull
    private long distance;

    public String getFormattedDistance() {
        return this.distance + "km";  // Method to return distance with " km"
    }
}
