package com.paralex.erp.exceptions;

public class VirtualCardServiceException extends RuntimeException {
    public VirtualCardServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
