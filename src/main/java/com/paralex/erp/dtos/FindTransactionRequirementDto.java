package com.paralex.erp.dtos;

import jakarta.annotation.Nullable;
import lombok.Data;

@Data
public class FindTransactionRequirementDto {
    @Nullable
    private Integer step;

    @Nullable
    private String transactionId;
}
