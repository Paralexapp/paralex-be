package com.paralex.erp.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class AddAdjournmentDateDto {
    @NotNull
    @NotEmpty
    @NotBlank
    private String bailBondId;

    @NotNull
    private LocalDate adjournmentDate;
}
