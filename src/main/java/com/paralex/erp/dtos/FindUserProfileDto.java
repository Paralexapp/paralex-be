package com.paralex.erp.dtos;

import jakarta.annotation.Nullable;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class FindUserProfileDto {
    @Nullable
    private String id;

    @Nullable
    private String email;

    @Nullable
    private String phoneNumber;
}
