package com.paralex.erp.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
public class TestController {

    @GetMapping()
    public String getStatus(){
        return "Paralex-app is up and running, time: "+ LocalDateTime.now();
    }
}
