package com.paralex.erp.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import static com.paralex.erp.ErpApplication.DEFAULT_PAGE_SIZE;

@Data
public class PaginatedRequestDto {
    @NotNull
    private Integer pageNumber;

    @NotNull
    private Integer pageSize;

    {
        this.pageSize = DEFAULT_PAGE_SIZE;
        this.pageNumber = 0;
    }
}
