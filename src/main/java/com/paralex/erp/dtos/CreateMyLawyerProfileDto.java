package com.paralex.erp.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class CreateMyLawyerProfileDto {
    @NotBlank
    @NotEmpty
    @NotBlank
    private String stateOfPractice;

    @NotBlank
    @NotEmpty
    @NotBlank
    private String supremeCourtNumber;

    @NotNull
    @NotEmpty
    private List<String> practiceAreas;

    @NotNull
    private double latitude;

    @NotNull
    private double longitude;
}
