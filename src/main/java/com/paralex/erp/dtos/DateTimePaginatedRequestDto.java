package com.paralex.erp.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
public class DateTimePaginatedRequestDto extends PaginatedRequestDto {
    @NotNull
    private LocalDate start;

    @NotNull
    private LocalDate end;
}
