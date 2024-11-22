package com.paralex.erp.exceptions;

public class DirectoryCreationFailedException extends RuntimeException{
    public DirectoryCreationFailedException(String message){
        super(message);
    }
}
