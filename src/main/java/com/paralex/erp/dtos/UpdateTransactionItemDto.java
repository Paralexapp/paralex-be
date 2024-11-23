package com.paralex.erp.dtos;

import jakarta.annotation.Nullable;
import lombok.Data;

@Data
public class UpdateTransactionItemDto {
    @Nullable
    private String name;

    @Nullable
    private Boolean status;
}
