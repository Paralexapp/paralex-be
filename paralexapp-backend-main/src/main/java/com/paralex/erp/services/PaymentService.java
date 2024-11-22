package com.paralex.erp.services;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@RequiredArgsConstructor
@Validated
@Service
@Log4j2
public class PaymentService {
    public void verifyTransactionByReference(@NotNull String id) {

    }
    
    public void getPaymentHistory() {}

    public void addPaymentHistory() {
    }
}
