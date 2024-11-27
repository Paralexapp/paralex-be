package com.paralex.erp.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateProfileDto {
//    private String paywiseUserName;;
//    private String bvn;
    private String email;
    private String phoneNumber;
    private String firstName;
    private String lastName;
    private String dateOfBirth;
}
