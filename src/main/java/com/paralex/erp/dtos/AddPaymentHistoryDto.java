package com.paralex.erp.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AddPaymentHistoryDto {
    @NotNull
    private long transactionId;

    @NotNull
    @NotEmpty
    @NotBlank
    private String transactionReference;

    @NotNull
    @NotEmpty
    @NotBlank
    private String domain;

    @NotNull
    @NotEmpty
    @NotBlank
    private String status;

    @NotNull
    @NotEmpty
    @NotBlank
    private String reference;

    @Nullable
    private Object receiptNumber;

    @NotNull
    private int amount;

    @NotNull
    @NotEmpty
    @NotBlank
    private String channel;

    @NotNull
    @NotEmpty
    @NotBlank
    private String target;

    @NotNull
    @NotEmpty
    @NotBlank
    private String targetId;

    @NotNull
    private VerifyTransactionByReferenceResponseDto gatewayResponse;
}
