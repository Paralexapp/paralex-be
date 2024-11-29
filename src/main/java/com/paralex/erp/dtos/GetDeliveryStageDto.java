package com.paralex.erp.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Builder
@EqualsAndHashCode(callSuper = true)
@Data
public class GetDeliveryStageDto extends PaginatedRequestDto {
    @NotNull
    private boolean forAdmin;

    @NotNull
    private boolean forDriver;
}
