package com.paralex.erp.controllers;

import com.paralex.erp.dtos.AddTransactionDto;
import com.paralex.erp.dtos.FindTransactionDto;
import com.paralex.erp.dtos.UpdateItemDto;
import com.paralex.erp.entities.TransactionEntity;
import com.paralex.erp.services.TransactionService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Transactions", description = "APIs to manage Transactions under Transaction Items (Entities, according to documentation) e.g Business Name (Transaction) under Company Registration (Transaction Items, Entities)")
@SecurityRequirement(name = "Header Token")
@SecurityRequirement(name = "Cookie Token")
@RequiredArgsConstructor
@Validated
@CrossOrigin
@RestController
@RequestMapping("/transaction")
@Log4j2
public class TransactionController {
    private final TransactionService transactionService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(
            value = "/",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public void createTransactions(@RequestBody @NotNull @NotEmpty List<AddTransactionDto> addTransactionDtoList) {
        transactionService.createTransactions(addTransactionDtoList);
    }

    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<TransactionEntity> getTransactions(@NotNull FindTransactionDto findTransactionDto) {
        return transactionService.getTransactions(findTransactionDto);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping(
            value = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public void updateTransaction(
            @PathVariable("id")
            @Parameter(name = "id", description = "A Transaction ID", example = "f8c3de3d-1fea-4d7c-a8b0-29f63c4c3454")
            @NotNull @NotEmpty @NotBlank String id,
            @RequestBody @NotNull List<UpdateItemDto> changes) {
        transactionService.updateTransaction(id, changes);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public void deleteTransaction(
            @PathVariable("id")
            @Parameter(name = "id", description = "A Transaction ID", example = "f8c3de3d-1fea-4d7c-a8b0-29f63c4c3454")
            @NotNull String id) {
        transactionService.deleteTransaction(id);
    }
}
