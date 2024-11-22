package com.paralex.erp.exceptions;

public class QrCodeCreationFailedException extends RuntimeException{
    public QrCodeCreationFailedException(String message){
        super(message);
    }
}
