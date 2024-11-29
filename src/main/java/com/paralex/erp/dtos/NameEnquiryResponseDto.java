package com.paralex.erp.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class NameEnquiryResponseDto {
    @NotNull
    @NotEmpty
    @NotBlank
    private String accountNumber;

    @NotNull
    @NotEmpty
    @NotBlank
    private String accountName;

    @NotNull
    @NotEmpty
    @NotBlank
    private String bankId;
}
