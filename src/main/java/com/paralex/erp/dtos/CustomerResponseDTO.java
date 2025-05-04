package com.paralex.erp.dtos;

import com.paralex.erp.enums.RegistrationLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@RequiredArgsConstructor
public class CustomerResponseDTO {

    private String id;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String userType;
    private String aboutMe;
    private RegistrationLevel registrationLevel;
    private String photoUrl;
    private String roles;
}
