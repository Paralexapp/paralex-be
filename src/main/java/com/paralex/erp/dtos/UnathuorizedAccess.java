package com.paralex.erp.dtos;

public class UnathuorizedAccess extends RuntimeException{
    public UnathuorizedAccess(String message){
        super(message);
    }
}
