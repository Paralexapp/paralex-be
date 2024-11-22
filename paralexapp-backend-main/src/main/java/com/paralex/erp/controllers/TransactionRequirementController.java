package com.paralex.erp.controllers;

import com.paralex.erp.dtos.AddTransactionRequirementDto;
import com.paralex.erp.dtos.AddTransactionRequirementOptionDto;
import com.paralex.erp.dtos.FindTransactionRequirementDto;
import com.paralex.erp.dtos.UpdateItemDto;
import com.paralex.erp.entities.TransactionRequirementEntity;
import com.paralex.erp.entities.TransactionRequirementOptionEntity;
import com.paralex.erp.services.TransactionRequirementService;
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
import java.util.UUID;

@Tag(name = "Transaction Requirement", description = "APIs to build the step/workflow for each transaction inclusive of requirements that might have options")
@SecurityRequirement(name = "Header Token")
@SecurityRequirement(name = "Cookie Token")
@RequiredArgsConstructor
@Validated
@CrossOrigin
@RestController
@RequestMapping("/transaction/requirement")
@Log4j2
public class TransactionRequirementController {
    private final TransactionRequirementService transactionRequirementService;

    @GetMapping(value = "/{id}/option", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<TransactionRequirementOptionEntity> getTransactionRequirementOptions(
            @PathVariable("id")
            @Parameter(name = "id", description = "A Transaction Requirement instance ID", example = "f8c3de3d-1fea-4d7c-a8b0-29f63c4c3454")
            @NotNull String transactionRequirementId) {
        return transactionRequirementService.getTransactionRequirementOptions(transactionRequirementId);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(
            value = "/option",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public void addRequirementOptions(@NotNull @NotEmpty List<AddTransactionRequirementOptionDto> addTransactionRequirementOptionDtoList) {
        transactionRequirementService.addRequirementOptions(addTransactionRequirementOptionDtoList);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping(
            value = "/option/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public void updateTransactionRequirementOption(
            @PathVariable("id")
            @Parameter(name = "id", description = "A Transaction Requirement Option ID", example = "f8c3de3d-1fea-4d7c-a8b0-29f63c4c3454")
            @NotNull @NotEmpty @NotBlank String id,
            @RequestBody @NotNull List<UpdateItemDto> changes) {
        transactionRequirementService.updateTransactionRequirementOption(id, changes);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/option/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public void deleteTransactionRequirementOption(
            @PathVariable("id")
            @Parameter(name = "id", description = "A Transaction Requirement Option ID", example = "f8c3de3d-1fea-4d7c-a8b0-29f63c4c3454")
            @NotNull String id) {
        transactionRequirementService.deleteTransactionRequirementOption(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(
            value = "/",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public void addTransactionRequirements(@RequestBody @NotNull List<AddTransactionRequirementDto> addTransactionRequirementDtoList) {
        transactionRequirementService.addTransactionRequirements(addTransactionRequirementDtoList);
    }

    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<TransactionRequirementEntity> getTransactionRequirements(@NotNull FindTransactionRequirementDto findTransactionRequirementDto) {
        return transactionRequirementService.getTransactionRequirements(findTransactionRequirementDto);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping(
            value = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public void updateTransactionRequirement(
            @PathVariable("id")
            @Parameter(name = "id", description = "A Transaction Requirement ID", example = "f8c3de3d-1fea-4d7c-a8b0-29f63c4c3454")
            @NotNull @NotEmpty @NotBlank String id,
            @RequestBody @NotNull List<UpdateItemDto> changes) {
        transactionRequirementService.updateTransactionRequirement(id, changes);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public void deleteTransactionRequirement(
            @PathVariable("id")
            @Parameter(name = "id", description = "A Transaction Requirement Option ID", example = "f8c3de3d-1fea-4d7c-a8b0-29f63c4c3454")
            @NotNull String id) {
        transactionRequirementService.deleteTransactionRequirement(id);
    }
}
