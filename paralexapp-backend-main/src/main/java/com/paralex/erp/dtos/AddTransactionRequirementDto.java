package com.paralex.erp.dtos;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class AddTransactionRequirementDto {
    @NotNull
    @NotEmpty
    @NotBlank
    private String label;

    @NotNull
    @NotEmpty
    @NotBlank
    private String type;

    @Nullable
    private String errorMessage;

    @NotNull
    @NotEmpty
    @NotBlank
    private String description;

    @NotNull
    private Integer index;

    @NotNull
    private Integer step;

    @Nullable
    private Boolean required;

    @Nullable
    private Boolean hasOption;

    @Nullable
    private Boolean multiple;

    @Nullable
    private Integer max;

    @Nullable
    private Integer min;

    @NotNull
    @NotEmpty
    @NotBlank
    private String transactionId;

    @Nullable
    private Boolean status;

    @Nullable
    private List<AddTransactionRequirementOptionDto> options = List.of();
}
