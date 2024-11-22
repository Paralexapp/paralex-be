package com.paralex.erp.controllers;

import com.paralex.erp.dtos.CreateCourtDivisionDto;
import com.paralex.erp.dtos.PaginatedRequestDto;
import com.paralex.erp.dtos.UpdateItemDto;
import com.paralex.erp.entities.CourtDivisionEntity;
import com.paralex.erp.services.CourtDivisionService;
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

@Tag(name = "Court Divisions", description = "APIs to manage Courts")
@SecurityRequirement(name = "Header Token")
@SecurityRequirement(name = "Cookie Token")
@RequiredArgsConstructor
@Validated
@CrossOrigin
@RestController
@RequestMapping("/court/division")
@Log4j2
public class CourtDivisionController {
    private final CourtDivisionService courtDivisionService;

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public void deleteCourtDivision(@PathVariable("id") @NotNull String id) {
        courtDivisionService.deleteCourtDivision(id);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping(
            value = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public void updateCourtDivision(
            @PathVariable("id") @NotNull @NotEmpty @NotBlank String id,
            @RequestBody @NotNull List<UpdateItemDto> changes) {
        courtDivisionService.updateCourtDivision(id, changes);
    }

    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<CourtDivisionEntity> getCourtDivisions(@NotNull PaginatedRequestDto paginatedRequestDto) {
        return courtDivisionService.getCourtDivisions(paginatedRequestDto);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(
            value = "/",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public void createCourtDivisions(@RequestBody @NotNull @NotEmpty List<CreateCourtDivisionDto> createCourtDivisionDtoList) {
        courtDivisionService.createCourtDivisions(createCourtDivisionDtoList);
    }
}
