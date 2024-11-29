package com.paralex.erp.services;

import com.paralex.erp.dtos.StatesByCountryDto;
import com.paralex.erp.entities.CountryEntity;
import com.paralex.erp.entities.StateEntity;
import com.paralex.erp.repositories.CountryRepository;
import com.paralex.erp.repositories.StateRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@RequiredArgsConstructor
@Validated
@Service
@Log4j2
public class CountryStateService {
    private final CountryRepository countryRepository;
    private final StateRepository stateRepository;

    public List<StateEntity> getAllStatesOfCountry(@NotNull StatesByCountryDto statesByCountryDto) {
        return stateRepository.findAll(Example.of(StateEntity.builder()
                        .countryCode(statesByCountryDto.getCountryCode())
                        .iso2(statesByCountryDto.getIso2())
                        .countryId(statesByCountryDto.getCountryId())
                .build(), ExampleMatcher.matchingAll()
                .withIgnoreNullValues()
                .withIgnorePaths("id", "latitude", "longitude")));
    }

    public List<CountryEntity> getAllCountries() {
        return countryRepository.findAll();
    }
}
