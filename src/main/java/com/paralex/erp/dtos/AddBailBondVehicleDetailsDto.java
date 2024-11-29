package com.paralex.erp.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddBailBondVehicleDetailsDto {
    @NotNull
    @NotEmpty
    @NotBlank
    private String year;

    @NotNull
    @NotEmpty
    @NotBlank
    private String make;

    @NotNull
    @NotEmpty
    @NotBlank
    private String model;

    @NotNull
    @NotEmpty
    @NotBlank
    private String color;

    @NotNull
    @NotEmpty
    @NotBlank
    private String plateNumber;

    @NotNull
    @NotEmpty
    @NotBlank
    private String state;

    @NotNull
    @NotEmpty
    @NotBlank
    private String insuranceCompanyOrAgent;
}
