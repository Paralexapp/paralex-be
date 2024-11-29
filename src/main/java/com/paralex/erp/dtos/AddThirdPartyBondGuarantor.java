package com.paralex.erp.dtos;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddThirdPartyBondGuarantor {
    @NotNull
    @NotEmpty
    @NotBlank
    private String name;

    @NotNull
    @NotEmpty
    @NotBlank
    private String currentAddress;

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
    private String nationalIdentityNumber;

    @Nullable
    private String internationalPassport;

    @Nullable
    private String driversLicense;

    @Nullable
    private String taxIdentityNumber;
}
