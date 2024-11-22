package com.paralex.erp.dtos;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddTransactionRequirementOptionDto {
    @NotNull
    @NotEmpty
    @NotBlank
    private String label;

    @NotNull
    private Boolean status;

    @NotNull
    @NotEmpty
    @NotBlank
    private String data;

    @Nullable
    private String transactionRequirementId;
}
