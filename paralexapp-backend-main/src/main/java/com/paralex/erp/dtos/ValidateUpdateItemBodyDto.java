package com.paralex.erp.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ValidateUpdateItemBodyDto {
    private String[] keysList;
    private Class<?> modelClass;
}
