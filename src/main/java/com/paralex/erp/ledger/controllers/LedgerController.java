package com.paralex.erp.ledger.controllers;

import com.paralex.erp.ledger.documents.LedgerFilterDTO;
import com.paralex.erp.ledger.documents.PageableLedgerDTO;
import com.paralex.erp.ledger.services.LedgerService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@CrossOrigin
@RestController
@RequestMapping("api/v1/ledger")
@RequiredArgsConstructor
public class LedgerController<T> {

    private final LedgerService service;
    @PostMapping(value = "get-excel", produces ="application/octet-stream")
    @ResponseBody
    public ResponseEntity<ByteArrayResource> getExcel(@RequestHeader(value="apiKey") String apiKey, @RequestBody(required = false) LedgerFilterDTO filterParams, HttpServletRequest req) throws IOException {
        InputStream inputStream = new FileInputStream(service.exportToExcel(filterParams,req));
        ByteArrayResource resource = new ByteArrayResource(IOUtils.toByteArray(inputStream),"transaction_history.xlsx");
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,"attachement;filename=transaction_history.xlsx")
                .contentLength(resource.contentLength())
                .body(resource);
    }

    @PostMapping(value = "get-csv", produces ="application/octet-stream")
    @ResponseBody
    public ResponseEntity<ByteArrayResource> getCsv(@RequestHeader(value="apiKey") String apiKey,@RequestBody(required = false)LedgerFilterDTO filterParams, HttpServletRequest req) throws IOException {
        InputStream inputStream = new FileInputStream(service.exportToCsv(filterParams,req));
        ByteArrayResource resource = new ByteArrayResource(IOUtils.toByteArray(inputStream),"transaction_history.csv");
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,"attachement;filename=transaction_history.csv")
                .contentLength(resource.contentLength())
                .body(resource);
    }

    @PostMapping("get")
    public T getLedger(@RequestHeader(value="apiKey") String apiKey,
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false,defaultValue = "10") int size,
            @RequestParam(value = "start", required = false,defaultValue = "0") int start,
            @RequestBody(required = false) PageableLedgerDTO filterParams,
            HttpServletRequest req){
        return (T) service.filterLeger(filterParams,page,size,start,req);
    }
}
