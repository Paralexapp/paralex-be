package com.paralex.erp.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class SendPaymentLinkResponseDto {
    @NotNull
    private int id;

    @NotNull
    @NotEmpty
    @NotBlank
    private String domain;

    @NotNull
    private int amount;

    @NotNull
    @NotEmpty
    @NotBlank
    private String currency;

    @NotNull
    @JsonProperty(value = "due_date")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime dueDate;

    @NotNull
    @JsonProperty(value = "has_invoice")
    private boolean hasInvoice;

    @NotNull
    @JsonProperty(value = "invoice_number")
    private int invoiceNumber;

    @NotNull
    @NotEmpty
    @NotBlank
    private String description;

    @Builder.Default
    @NotNull
    @NotEmpty
    private List<SendPaymentLinkDto.LineItemDto> lineItems = List.of();

    @Builder.Default
    @NotNull
    @NotEmpty
    private List<SendPaymentLinkDto.TaxDto> tax = List.of();

    @NotNull
    @NotEmpty
    @NotBlank
    @JsonProperty(value = "requested_code")
    private String requestCode;

    @NotNull
    @NotEmpty
    @NotBlank
    private String status;

    @NotNull
    private boolean paid;

    @NotNull
    @NotEmpty
    @NotBlank
    private String customer;

    @NotNull
    @JsonProperty(value = "created_at")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime createdAt;
}
