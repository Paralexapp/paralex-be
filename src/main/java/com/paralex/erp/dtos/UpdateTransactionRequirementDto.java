package com.paralex.erp.dtos;

import jakarta.annotation.Nullable;
import lombok.Data;

@Data
public class UpdateTransactionRequirementDto {
    @Nullable
    private String label;

    @Nullable
    private String errorMessage;

    @Nullable
    private String description;

    @Nullable
    private Integer index;

    @Nullable
    private Integer group;

    @Nullable
    private Boolean required;

    @Nullable
    private Boolean multiple;

    @Nullable
    private Integer max;

    @Nullable
    private Integer min;
}
