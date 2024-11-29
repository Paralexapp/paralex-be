package com.paralex.erp.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class CreateLawFirmDto {
    @NotNull
    @NotEmpty
    @NotBlank
    private String name;

    @NotNull
    private double latitude;

    @NotNull
    private double longitude;

    @NotNull
    @NotEmpty
    private List<String> practiceAreas;
}
