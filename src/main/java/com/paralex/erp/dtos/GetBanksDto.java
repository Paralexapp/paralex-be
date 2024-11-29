package com.paralex.erp.dtos;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class GetBanksDto {
    @Nullable
    private String country;

    @Max(100)
    @Min(1)
    @NotNull
    private int pageSize;

    @Nullable
    private String next;

    @Nullable
    private String previous;
}
