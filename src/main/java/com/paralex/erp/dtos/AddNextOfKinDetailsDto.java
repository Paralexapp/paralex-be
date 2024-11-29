package com.paralex.erp.dtos;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddNextOfKinDetailsDto {
    @NotNull
    @NotEmpty
    @NotBlank
    private String name;

    @NotNull
    @NotEmpty
    @NotBlank
    private String relationship;

    @NotNull
    @NotEmpty
    @NotBlank
    private String homePhone;

    @Nullable
    private String workPhone;

    @NotNull
    @NotEmpty
    @NotBlank
    private String email;

    @NotNull
    @NotEmpty
    @NotBlank
    private String nationalIdentityNumber;

    @NotNull
    @NotEmpty
    @NotBlank
    private String driversLicense;

    @Nullable
    private String internationalPassport;

    @NotNull
    @NotEmpty
    @NotBlank
    private String homeAddress;

    @NotNull
    @NotEmpty
    @NotBlank
    private String occupation;

    @NotNull
    @NotEmpty
    @NotBlank
    private String currentEmployer;

    @NotNull
    @NotEmpty
    @NotBlank
    private String currentEmployerAddress;

    @NotNull
    @NotEmpty
    @NotBlank
    private String supervisorsName;

    @NotNull
    @NotEmpty
    @NotBlank
    private String supervisorsPhoneNumber;
}
