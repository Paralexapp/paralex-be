package com.paralex.erp.dtos;

import jakarta.annotation.Nullable;
import lombok.Data;

@Data
public class UpdateTransactionDto {
    @Nullable
    private String name;

    @Nullable
    private Boolean status;

    @Nullable
    private Integer amount;
}
