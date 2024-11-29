package com.paralex.erp.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class PaymentGatewayNameEnquiryResponseDto {
    @NotNull
    @NotEmpty
    @NotBlank
    @JsonProperty("account_number")
    private String accountNumber;

    @NotNull
    @NotEmpty
    @NotBlank
    @JsonProperty("account_name")
    private String accountName;

    @NotNull
    @NotEmpty
    @NotBlank
    @JsonProperty("bank_id")
    private String bankId;
}
