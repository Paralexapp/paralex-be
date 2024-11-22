package com.paralex.erp.dtos;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateTransactionRequestSubmissionDto {
    @Nullable
    private Object value;

    @NotNull
    @NotEmpty
    @NotBlank
    private String label;

    @NotNull
    @NotEmpty
    @NotBlank
    private String transactionRequirementId;

    @Nullable
    private String optionId;

    @Nullable
    private String optionLabel;
}
