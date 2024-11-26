package com.paralex.erp.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistrationDto {
    private String password;
    private String confirmPassword;
    private String userType;
    private String email;

}
