package com.paralex.erp.controllers;

import com.paralex.erp.dtos.AddLawPracticeAreaDto;
import com.paralex.erp.dtos.PaginatedRequestDto;
import com.paralex.erp.dtos.UpdateItemDto;
import com.paralex.erp.entities.LawPracticeAreaEntity;
import com.paralex.erp.services.LawPracticeAreaService;
import io.swagger.v3.oas.annotations.Operation;
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

@Tag(name = "Law Practice Areas", description = "APIs to manage law practice areas for use by Admin and Laws")
@SecurityRequirement(name = "Header Token")
@SecurityRequirement(name = "Cookie Token")
@RequiredArgsConstructor
@Validated
@CrossOrigin
@RestController
@RequestMapping("/service-provider/law/practice-area")
@Log4j2
public class LawPracticeAreaController {
    private final LawPracticeAreaService lawPracticeAreaService;

    @Operation(summary = "Delete Law Practice Area",
            description = "Totally delete an instance of a law practice area")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public void deleteLawPracticeArea(
            @PathVariable("id")
            @Parameter(name = "id", description = "A law practice area instance ID", example = "f8c3de3d-1fea-4d7c-a8b0-29f63c4c3454")
            @NotNull String id) {
        lawPracticeAreaService.deleteLawPracticeArea(id);
    }

    @Operation(summary = "Update Law Practice Area",
            description = "Make changes to attribute of this instance of a law practice area")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping(
            value = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public void updateLawPracticeArea(
            @PathVariable("id")
            @Parameter(name = "id", description = "A law practice area instance ID", example = "f8c3de3d-1fea-4d7c-a8b0-29f63c4c3454")
            @NotNull @NotEmpty @NotBlank String id,
            @RequestBody @NotNull List<UpdateItemDto> changes) {
        lawPracticeAreaService.updateLawPracticeArea(id, changes);
    }

    @Operation(summary = "Get Law Practice Areas",
            description = "Retrieve the list of law practice areas.")
    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<LawPracticeAreaEntity> getLawPracticeAreas(@NotNull PaginatedRequestDto paginatedRequestDto) {
        return lawPracticeAreaService.getLawPracticeAreas(paginatedRequestDto);
    }

    @Operation(summary = "Create Law Practice Area(s)",
            description = "Create a single or bulk of law practise areas for laws. It returns nothing.")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(
            value = "/",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public void addLawPracticeAreas(@RequestBody @NotNull @NotEmpty List<AddLawPracticeAreaDto> addLawPracticeAreaDtoList) {
        lawPracticeAreaService.addLawPracticeAreas(addLawPracticeAreaDtoList);
    }
}
