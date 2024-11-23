package com.paralex.erp.dtos;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Data
public class CountDto {
    @Nullable
    private LocalDateTime startDate;

    @Nullable
    private LocalDateTime endDate;
}
