package com.paralex.erp.exceptions;

public class IncorrectTransactionPinException extends RuntimeException {
    public IncorrectTransactionPinException(String message) {
        super(message);
    }
}
