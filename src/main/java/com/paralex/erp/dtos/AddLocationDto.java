package com.paralex.erp.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddLocationDto {
    @NotNull
    @NotEmpty
    @NotBlank
    private String name;

    @NotNull
    private Boolean status;

    @NotNull
    private double latitude;

    @NotNull
    private double longitude;

    @NotNull
    private int amount;
}
