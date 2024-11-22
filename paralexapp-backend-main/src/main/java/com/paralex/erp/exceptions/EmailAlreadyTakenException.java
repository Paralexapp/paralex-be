package com.paralex.erp.exceptions;

public class EmailAlreadyTakenException extends RuntimeException{

    public EmailAlreadyTakenException(String message){
        super(message);
    }
}
