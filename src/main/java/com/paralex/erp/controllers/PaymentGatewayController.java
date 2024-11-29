package com.paralex.erp.controllers;

import com.paralex.erp.dtos.BankDto;
import com.paralex.erp.dtos.GetBanksDto;
import com.paralex.erp.dtos.NameEnquiryDto;
import com.paralex.erp.dtos.NameEnquiryResponseDto;
import com.paralex.erp.services.PaymentGatewayService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Payment Gateway", description = "APIs to get banks and others...")
@SecurityRequirement(name = "Header Token")
@SecurityRequirement(name = "Cookie Token")
@RequiredArgsConstructor
@Validated
@CrossOrigin
@RestController
@RequestMapping("/payment-gateway")
@Log4j2
public class PaymentGatewayController {
    private final PaymentGatewayService paymentGatewayService;

    @GetMapping(value = "/name-enquiry", produces = MediaType.APPLICATION_JSON_VALUE)
    public NameEnquiryResponseDto nameEnquiry(@NotNull NameEnquiryDto nameEnquiryDto) {
        return paymentGatewayService.nameEnquiry(nameEnquiryDto);
    }

    @GetMapping(value = "/banks", produces = MediaType.APPLICATION_JSON_VALUE)
    public BankDto getBanks(@NotNull GetBanksDto getBanksDto) {
        return paymentGatewayService.getBanks(getBanksDto);
    }
}
