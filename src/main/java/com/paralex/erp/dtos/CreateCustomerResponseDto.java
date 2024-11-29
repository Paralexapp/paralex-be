package com.paralex.erp.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class CreateCustomerResponseDto {
    @NotNull
    @NotEmpty
    @NotBlank
    public String email;

    @NotNull
    public int integration;

    @NotNull
    @NotEmpty
    @NotBlank
    public String domain;

    @NotNull
    @NotEmpty
    @NotBlank
    @JsonProperty(value = "customer_code")
    public String customerCode;

    @NotNull
    public int id;

    @NotNull
    public boolean identified;

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    public LocalDate createdAt;

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    public LocalDate updatedAt;
}
