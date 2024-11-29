package com.paralex.erp.dtos;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddBailBondSpouseDetailsDto {
    @NotNull
    @NotEmpty
    @NotBlank
    private String name;

    @NotNull
    @NotEmpty
    @NotBlank
    private String durationOfMarriage;

    @NotNull
    @NotEmpty
    @NotBlank
    private String address;

    @Nullable
    private String homePhone;

    @NotNull
    @NotEmpty
    @NotBlank
    private String mobilePhone;

    @Nullable
    private String workPhone;

    @NotNull
    @NotEmpty
    @NotBlank
    private String nin;

    @NotNull
    @NotEmpty
    @NotBlank
    private String driversLicense;

    @NotNull
    @NotEmpty
    @NotBlank
    private String internationalPassport;

    @NotNull
    @NotEmpty
    @NotBlank
    private String occupation;

    @NotNull
    @NotEmpty
    @NotBlank
    private String employer;

    @NotNull
    @NotEmpty
    @NotBlank
    private String durationOfEmployment;

    @NotNull
    @NotEmpty
    @NotBlank
    private String supervisorName;
}
