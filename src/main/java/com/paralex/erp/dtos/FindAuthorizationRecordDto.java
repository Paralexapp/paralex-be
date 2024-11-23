package com.paralex.erp.dtos;

import jakarta.annotation.Nullable;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class FindAuthorizationRecordDto extends PaginatedRequestDto {
    @Nullable
    private String resource;

    @Nullable
    private String status;

    @Nullable
    private String principal;
}
