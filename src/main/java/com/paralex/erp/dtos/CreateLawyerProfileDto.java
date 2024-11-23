package com.paralex.erp.dtos;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class CreateLawyerProfileDto extends CreateProfileForUserDto {
    private String supremeCourtNumber;
}
