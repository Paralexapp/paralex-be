package com.paralex.erp.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class EmailDto {
    private String recipient;
    private String messageBody;
    private String subject;
    private String attachment;
}
