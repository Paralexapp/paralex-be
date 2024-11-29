package com.paralex.erp.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class AddAuthorizationRecordDto {
    @NotNull
    @NotEmpty
    @NotBlank
    private String resource;

    @NotNull
    @NotEmpty
    @NotBlank
    private String status;

    @NotNull
    @NotEmpty
    @NotBlank
    private String action;

    @NotNull
    @NotEmpty
    @NotBlank
    private String principal;

    @NotNull
    @NotEmpty
    @NotBlank
    private List<String> targets;
}
