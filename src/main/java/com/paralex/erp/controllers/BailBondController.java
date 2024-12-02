package com.paralex.erp.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.paralex.erp.dtos.AddAdjournmentDateDto;
import com.paralex.erp.dtos.GlobalResponse;
import com.paralex.erp.dtos.PaginatedRequestDto;
import com.paralex.erp.dtos.SubmitBailBondRequestDto;
import com.paralex.erp.entities.BailBondEntity;
import com.paralex.erp.services.BailBondService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Bail Bond", description = "APIs to submit Bail Bond request, retrieve, and manage requests by admin")
@SecurityRequirement(name = "Header Token")
@SecurityRequirement(name = "Cookie Token")
@RequiredArgsConstructor
@Validated
@CrossOrigin
@RestController
@RequestMapping("/bail-bond")
@Log4j2
public class BailBondController {
    private final BailBondService bailBondService;

    @DeleteMapping(value = "/adjournment-date/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public void removeAdjournmentDate(@PathVariable("id") @NotNull String id) {
        bailBondService.removeAdjournmentDate(id);
    }

    @PostMapping(value = "/adjournment-date",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public void addAdjournmentDate(@RequestBody @NotNull AddAdjournmentDateDto addAdjournmentDateDto) {
        bailBondService.addAdjournmentDate(addAdjournmentDateDto);
    }

    @PostMapping(value = "/approve/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public void approveBailBondRequest(@PathVariable("id") @NotNull String id) throws JsonProcessingException {
        bailBondService.approveBailBondRequest(id);
    }

    @PostMapping(value = "/reject/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public void rejectBailBondRequest(@PathVariable("id") @NotNull String id) {
        bailBondService.rejectBailBondRequest(id);
    }

    @PostMapping(value = "/withdraw/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public void withdrawBailBondRequest(@PathVariable("id") @NotNull String id) {
        bailBondService.withdrawBailBondRequest(id);
    }

    @GetMapping(value = "/mine",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<BailBondEntity> getMyBailBondRequests(@NotNull PaginatedRequestDto paginatedRequestDto) {
        return bailBondService.getMyBailBondRequests(paginatedRequestDto);
    }

    @GetMapping(value = "/",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<BailBondEntity> getBailBondRequests(@NotNull PaginatedRequestDto paginatedRequestDto) {
        return bailBondService.getBailBondRequests(paginatedRequestDto);
    }

    @PostMapping(
            value = "/",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public GlobalResponse<?> submitBailBondRequest(
            @RequestBody @NotNull SubmitBailBondRequestDto submitBailBondRequestDto) {
        return bailBondService.submitBailBondRequest(submitBailBondRequestDto);


    }
}
