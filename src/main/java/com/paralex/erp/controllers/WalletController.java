package com.paralex.erp.controllers;

import com.paralex.erp.dtos.PaginatedRequestDto;
import com.paralex.erp.dtos.TopUpWalletDto;
import com.paralex.erp.dtos.WalletBalanceDto;
import com.paralex.erp.entities.WalletTransactionEntity;
import com.paralex.erp.services.WalletService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Tag(name = "Wallets", description = "APIs to top-up, find, balance...")
@SecurityRequirement(name = "Header Token")
@SecurityRequirement(name = "Cookie Token")
@RequiredArgsConstructor
@Validated
@CrossOrigin
@RestController
@RequestMapping("/wallet")
@Log4j2
public class WalletController {
    private final WalletService walletService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/")
    public WalletTransactionEntity topUpWallet(@NotNull TopUpWalletDto topUpWalletDto) throws MessagingException, IOException {
        return walletService.topUpWallet(topUpWalletDto);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/transactions")
    public List<WalletTransactionEntity> getWalletTransactions(@NotNull PaginatedRequestDto paginatedRequestDto) {
        return walletService.getWalletTransactions(paginatedRequestDto);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/balance")
    public WalletBalanceDto getWalletBalance() {
        return walletService.getWalletBalance();
    }
}
