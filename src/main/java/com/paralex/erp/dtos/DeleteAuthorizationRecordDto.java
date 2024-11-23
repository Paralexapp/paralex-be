package com.paralex.erp.dtos;

import jakarta.annotation.Nullable;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class DeleteAuthorizationRecordDto {
    @Nullable
    private String resource;

    @Nullable
    private String action;

    @Nullable
    private String status;

    @Nullable
    private String principal;
}
