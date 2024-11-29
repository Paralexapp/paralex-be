package com.paralex.erp.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class AddBailBondLandDetailsDto {
    @NotNull
    @NotEmpty
    @NotBlank
    private String address;

    @NotNull
    @NotEmpty
    @NotBlank
    private String state;

    @NotNull
    private LocalDate dateOfPurchase;

    @NotNull
    @NotEmpty
    @NotBlank
    private String certificateNumber;
}
