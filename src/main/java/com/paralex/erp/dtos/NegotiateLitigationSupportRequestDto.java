package com.paralex.erp.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class NegotiateLitigationSupportRequestDto {
    @NotNull
    private int proposedAmount;
}
