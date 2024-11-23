package com.paralex.erp.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class DateTimeRequestDto {
    @NotNull
    private LocalDate start;

    @NotNull
    private LocalDate end;
}
