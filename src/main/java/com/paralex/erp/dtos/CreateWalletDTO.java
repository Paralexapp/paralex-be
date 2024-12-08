package com.paralex.erp.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateWalletDTO {
    private String employeeId;
    private String businessId;
    private String phoneNumber;
    private String name;
    private String accountType;
    private String email;
}
