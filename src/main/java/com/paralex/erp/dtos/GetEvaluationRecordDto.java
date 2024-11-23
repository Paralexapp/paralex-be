package com.paralex.erp.dtos;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class GetEvaluationRecordDto {
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

    @Nullable
    private String principal;
}
