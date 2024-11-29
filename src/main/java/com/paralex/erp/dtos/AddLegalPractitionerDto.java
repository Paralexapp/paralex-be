package com.paralex.erp.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddLegalPractitionerDto {
    @NotNull
    @NotEmpty
    @NotBlank
    private String representativeName;

    @NotNull
    @NotEmpty
    @NotBlank
    private String nameOfFirm;

    @NotNull
    @NotEmpty
    @NotBlank
    private String address;

    @NotNull
    @NotEmpty
    @NotBlank
    private String phoneNumber;
}
