package com.paralex.erp.dtos;

import jakarta.annotation.Nullable;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class FindTransactionDto extends PaginatedRequestDto {
   @Nullable
    private String transactionItemId;
}
