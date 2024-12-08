package com.paralex.erp.entities;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.paralex.erp.commons.utils.Money;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Document(collection = "wallet")
@Data
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)

public class NewWallet extends BaseCollection {
    @Id
    private String walletId;
    private String employeeId;
    private String businessId;
    private String email;
    private Money balance = new Money();
    private String phoneNumber;
    private String firstname;
    private String lastname;
    private Money ledgerBalance = new Money();
    private String name;
    private String payStackVirtualAccountNumber;
    private String payStackVirtualAccountReference;
    private String payStackVirtualBankName;
    private String payStackCustomerCode;
    private String accountName;
    private String payStackVirtualBankCode;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String trxPin;
    private String type;
    private BigDecimal dailyWithdrawal = new BigDecimal("0");
    private boolean isBlocked = false;

}
