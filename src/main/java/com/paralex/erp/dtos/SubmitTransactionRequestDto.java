package com.paralex.erp.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class SubmitTransactionRequestDto {
    @NotNull
    @NotEmpty
    @NotBlank
    private String transactionId;

    @NotNull
    @NotEmpty
    @NotBlank
    private String transactionReference;

    @NotNull
    private int amountPaid;

    @NotNull
    @NotEmpty
    private List<CreateTransactionRequestSubmissionDto> submissions;
}
