package com.paralex.erp.controllers;

import com.paralex.erp.dtos.StatesByCountryDto;
import com.paralex.erp.entities.CountryEntity;
import com.paralex.erp.entities.StateEntity;
import com.paralex.erp.services.CountryStateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Lawyer Profiles", description = "APIs to create, retrieve, disable and enable Lawyer profiles")
@RequiredArgsConstructor
@Validated
@CrossOrigin
@RestController
@RequestMapping("/country-state")
@Log4j2
public class CountryStateController {
    private final CountryStateService countryStateService;

    @Operation(summary = "States",
            description = "Get states of a country...")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(
            value = "/states",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<StateEntity> getAllStatesOfCountry(@NotNull StatesByCountryDto statesByCountryDto) {
        return countryStateService.getAllStatesOfCountry((statesByCountryDto));
    }

    @Operation(summary = "Countries",
            description = "Get countries of the world!")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(
            value = "/countries",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<CountryEntity> getAllCountries() {
        return countryStateService.getAllCountries();
    }
}
