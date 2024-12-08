package com.paralex.erp.commons.utils;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.Currency;

@RequiredArgsConstructor
@Data
public class Money {
    private BigDecimal amount = new BigDecimal("0");
    private Currency currency = Currency.getInstance("NGN");
}
