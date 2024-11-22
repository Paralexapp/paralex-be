package com.paralex.erp.exceptions;

public class ForbiddenUpdateItemException extends RuntimeException {
    public ForbiddenUpdateItemException(String message) {
        super(message);
    }
}
