package com.paralex.erp.controllers;

import com.paralex.erp.dtos.DateTimePaginatedRequestDto;
import com.paralex.erp.dtos.DateTimeRequestDto;
import com.paralex.erp.dtos.SubmitTransactionRequestDto;
import com.paralex.erp.dtos.TransactionRequestDto;
import com.paralex.erp.services.TransactionRequestService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Tag(name = "Transaction Request", description = "APIs to manage, retrieve, submit Customer Transaction Requests i.e submissions of values for fields for a transaction registration as it were")
@SecurityRequirement(name = "Header Token")
@SecurityRequirement(name = "Cookie Token")
@RequiredArgsConstructor
@Validated
@CrossOrigin
@RestController
@RequestMapping("/transaction/request")
@Log4j2
public class TransactionRequestController {
    private final TransactionRequestService transactionRequestService;

    // TODO add interceptor for download with token in query param
    // https://ferncode.com/return-csv-file-for-spring-rest-service/
    // https://stackoverflow.com/a/67609568/10704082
    // https://developer.mozilla.org/en-US/docs/Web/HTTP/Basics_of_HTTP/MIME_types/Common_types
    @PutMapping(value = "/download")
    public ResponseEntity<Object> downloadTransactions(@NotNull DateTimeRequestDto dateTimeRequestDto) throws IOException {
        final var result = transactionRequestService.downloadTransactions(dateTimeRequestDto);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE)
                .contentType(MediaType.parseMediaType("text/csv"))
                .header(HttpHeaders.CONTENT_DISPOSITION)
                .contentType(MediaType.parseMediaType("attachment; filename=transactions-" + dateTimeRequestDto.getEnd() + "-" + dateTimeRequestDto.getEnd() + ".csv"))
                .body(result.getBytes());
    }

    @PutMapping(value = "/suspend", produces = MediaType.APPLICATION_JSON_VALUE)
    public void suspendTransactionRequest(
            @PathVariable("id")
            @Parameter(name = "id", description = "A Transaction Request ID", example = "f8c3de3d-1fea-4d7c-a8b0-29f63c4c3454")
            @NotNull String id) {
        transactionRequestService.suspendTransactionRequest(id);
    }

    @PutMapping(value = "/process", produces = MediaType.APPLICATION_JSON_VALUE)
    public void processTransactionRequest(
            @PathVariable("id")
            @Parameter(name = "id", description = "A Transaction Request ID", example = "f8c3de3d-1fea-4d7c-a8b0-29f63c4c3454")
            @NotNull String id) {
        transactionRequestService.processTransactionRequest(id);
    }

    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<TransactionRequestDto> getTransactionRequests(@NotNull DateTimeRequestDto dateTimeRequestDto) {
        return transactionRequestService.getTransactionRequests(dateTimeRequestDto);
    }

    @GetMapping(value = "/mine", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<TransactionRequestDto> getMyTransactionRequests(@NotNull DateTimePaginatedRequestDto dateTimePaginatedRequestDto) {
        return transactionRequestService.getMyTransactionRequests(dateTimePaginatedRequestDto);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(
            value = "/",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public void submitTransactionRequest(@RequestBody @NotNull SubmitTransactionRequestDto submitTransactionRequestDto
    ) {
        transactionRequestService.submitTransactionRequest(submitTransactionRequestDto);
    }
}
