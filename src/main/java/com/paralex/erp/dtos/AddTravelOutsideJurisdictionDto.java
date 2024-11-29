package com.paralex.erp.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddTravelOutsideJurisdictionDto {
    @NotNull
    @NotEmpty
    @NotBlank
    private String lastTravelOutsideJurisdiction;

    @NotNull
    @NotEmpty
    @NotBlank
    private String durationOfTrip;

    @NotNull
    @NotEmpty
    @NotBlank
    private String destination;

    @NotNull
    @NotEmpty
    @NotBlank
    private String nextPlannedTripOutsideJurisdiction;

    @NotNull
    @NotEmpty
    @NotBlank
    private String destinationOfPlannedTrip;

    @NotNull
    @NotEmpty
    @NotBlank
    private String nextOutOfCountryTrip;

    @NotNull
    @NotEmpty
    @NotBlank
    private String nextOutOfCountryDestination;
}
