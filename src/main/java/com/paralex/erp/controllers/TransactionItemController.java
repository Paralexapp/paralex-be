package com.paralex.erp.controllers;

import com.paralex.erp.dtos.CreateTransactionItemDto;
import com.paralex.erp.dtos.PaginatedRequestDto;
import com.paralex.erp.dtos.UpdateItemDto;
import com.paralex.erp.services.TransactionItemService;
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

@Tag(name = "Transaction Items (Entities)", description = "APIs to manage Transactions Items (Entities, according to documentation) e.g Company Registration (Transaction Items, Entities)")
@SecurityRequirement(name = "Header Token")
@SecurityRequirement(name = "Cookie Token")
@RequiredArgsConstructor
@Validated
@CrossOrigin
@RestController
@RequestMapping("/transaction/item")
@Log4j2
public class TransactionItemController {
    private final TransactionItemService transactionItemService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(
            value = "/",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public void createTransactionItems(@RequestBody @NotNull @NotEmpty List<CreateTransactionItemDto> createTransactionItemDtoList) {
        transactionItemService.createTransactionItems(createTransactionItemDtoList);
    }

    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public void getTransactionItems(@NotNull PaginatedRequestDto paginatedRequestDto) {
        transactionItemService.getTransactionItems(paginatedRequestDto);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping(
            value = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public void updateTransactionItem(
            @PathVariable("id")
            @Parameter(name = "id", description = "A Transaction Item ID", example = "f8c3de3d-1fea-4d7c-a8b0-29f63c4c3454")
            @NotNull @NotEmpty @NotBlank String id,
            @RequestBody @NotNull List<UpdateItemDto> changes) {
        transactionItemService.updateTransactionItem(id, changes);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public void deleteTransactionItem(
            @PathVariable("id")
            @Parameter(name = "id", description = "A Transaction Item ID", example = "f8c3de3d-1fea-4d7c-a8b0-29f63c4c3454")
            @NotNull @NotEmpty @NotBlank String id) {
        transactionItemService.deleteTransactionItem(id);
    }
}
