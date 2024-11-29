package com.paralex.erp.dtos;

import jakarta.annotation.Nullable;
import lombok.Data;

@Data
public class FindLawyerProfileDto {
    @Nullable
    private String email;

    @Nullable
    private String phoneNumber;
}
