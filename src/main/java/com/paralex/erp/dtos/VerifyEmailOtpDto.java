package com.paralex.erp.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class VerifyEmailOtpDto {
    @NotNull
    @NotEmpty
    @NotBlank
    private String code;

    @NotNull
    @NotEmpty
    @NotBlank
    private String idToken;
}
