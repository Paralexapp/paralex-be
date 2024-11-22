package com.paralex.erp.controllers;

import com.paralex.erp.dtos.GlobalResponse;
import com.paralex.erp.dtos.SendOtpDto;
import com.paralex.erp.services.OtpService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@RestController
public class OtpController {
    private final OtpService otpService;

    @PostMapping(value = "send-otp" ,produces = MediaType.APPLICATION_JSON_VALUE)
    public GlobalResponse<?> sendOtp(@RequestBody SendOtpDto otp) {
        return otpService.sendOtp(otp);
    }
}
