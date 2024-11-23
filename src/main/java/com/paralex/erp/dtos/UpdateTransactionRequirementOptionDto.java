package com.paralex.erp.dtos;

import jakarta.annotation.Nullable;
import lombok.Data;

@Data
public class UpdateTransactionRequirementOptionDto {
    @Nullable
    private String label;

    @Nullable
    private Boolean status;

    @Nullable
    private String value;
}
