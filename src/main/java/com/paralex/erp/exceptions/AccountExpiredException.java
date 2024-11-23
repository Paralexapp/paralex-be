package com.paralex.erp.exceptions;

public class AccountExpiredException extends RuntimeException{
    public AccountExpiredException(String message) {
        super(message);
    }
    
}
