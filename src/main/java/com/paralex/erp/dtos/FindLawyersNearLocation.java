package com.paralex.erp.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class FindLawyersNearLocation extends PaginatedRequestDto {
    @NotNull
    private double latitude;

    @NotNull
    private double longitude;
}
