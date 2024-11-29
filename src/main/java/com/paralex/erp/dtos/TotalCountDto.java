package com.paralex.erp.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class TotalCountDto {
    @NotNull
    private Long total;
}
