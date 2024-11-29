package com.paralex.erp.controllers;

import com.paralex.erp.annotations.AuthorizationPolicy;
import com.paralex.erp.annotations.RequiredAuthorizationRecord;
import com.paralex.erp.dtos.*;
import com.paralex.erp.entities.LawFirmEntity;
import com.paralex.erp.services.LawFirmService;
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
import java.util.Optional;

@Tag(name = "Law Firms", description = "APIs to create law firms and get user law firm")
@SecurityRequirement(name = "Header Token")
@SecurityRequirement(name = "Cookie Token")
@RequiredArgsConstructor
@Validated
@CrossOrigin
@RestController
@RequestMapping("/service-provider/law-firm")
@Log4j2
public class LawFirmController {
    private final LawFirmService lawFirmService;

    @Operation(summary = "Law Firm Count",
            description = "Get the total count of law firms.")
    @SecurityRequirement(name = "Header Token")
    @SecurityRequirement(name = "Cookie Token")
    @AuthorizationPolicy(records = {
            @RequiredAuthorizationRecord(status = "Allow", resource = "UserCount", action = "Read"),
            @RequiredAuthorizationRecord(status = "Allow", resource = "PrincipalAdmin", action = "Read") })
    @GetMapping(
            value = "/count",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public TotalCountDto countDriverBy(CountDto countDto) {
        var total = lawFirmService.countAllOrBetweenTime(countDto);

        return TotalCountDto
                .builder()
                .total(total)
                .build();
    }

    @Operation(summary = "Search Law Firm",
            description = "Search for law firms around the provided location")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<LawFirmEntity> findLawFirmsAroundLocation(@NotNull FindLawyersNearLocation findLawyersNearLocation) {
        return lawFirmService.findLawFirmsAroundLocation(findLawyersNearLocation);
    }

    @Operation(summary = "Delete Law Firm Member",
            description = "Totally delete an instance of a law firm member membership record")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/member/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public void removeLawFirmMember(
            @PathVariable("id")
            @Parameter(name = "id", description = "An instance of a law firm member membership instance ID", example = "f8c3de3d-1fea-4d7c-a8b0-29f63c4c3454")
            @NotNull String id) {
        lawFirmService.removeLawFirmMember(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(
            value = "/member",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public void addLawFirmMember(@RequestBody @NotNull @NotEmpty List<AddLawFirmMemberDto> addLawFirmMemberDtoList) {
        lawFirmService.addLawFirmMember(addLawFirmMemberDtoList);
    }

    @Operation(summary = "Delete Law Firm Practice Area",
            description = "Totally delete an instance of a law firm practice area")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/practice-area/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public void deleteLawFirmPracticeArea(
            @PathVariable("id")
            @Parameter(name = "id", description = "A law firm practice area mapping instance ID", example = "f8c3de3d-1fea-4d7c-a8b0-29f63c4c3454")
            @NotNull String id) {
        lawFirmService.deleteLawFirmPracticeArea(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(
            value = "/practice-area",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public void addLawFirmPracticeAreas(@RequestBody @NotNull @NotEmpty List<AddLawFirmPracticeAreaDto> addLawFirmPracticeAreaDtoList) {
        lawFirmService.addLawFirmPracticeAreas(addLawFirmPracticeAreaDtoList);
    }

    @Operation(summary = "Delete Law Firm",
            description = "Totally delete an instance of a law firm")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public void deleteLawFirm(
            @PathVariable("id")
            @Parameter(name = "id", description = "A law firm instance ID", example = "f8c3de3d-1fea-4d7c-a8b0-29f63c4c3454")
            @NotNull String id) {
        lawFirmService.deleteLawFirm(id);
    }

    @Operation(summary = "Update Law Firm",
            description = "Make changes to attribute of this instance of a law firm")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping(
            value = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public void updateLawFirm(
            @PathVariable("id")
            @Parameter(name = "id", description = "A law firm area instance ID", example = "f8c3de3d-1fea-4d7c-a8b0-29f63c4c3454")
            @NotNull @NotEmpty @NotBlank String id,
            @RequestBody @NotNull List<UpdateItemDto> changes) {
        lawFirmService.updateLawFirm(id, changes);
    }

    @GetMapping(value = "/mine", produces = MediaType.APPLICATION_JSON_VALUE)
    public Optional<LawFirmEntity> getMyLawFirm() {
        return lawFirmService.getMyLawFirm();
    }

    @GetMapping(value = "/")
    public List<LawFirmEntity> getLawFirms(@NotNull PaginatedRequestDto paginatedRequestDto) {
        return lawFirmService.getLawFirms(paginatedRequestDto);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping(value = "/")
    public void createLawFirm(@RequestBody @NotNull CreateLawFirmDto createLawFirmDto) {
        lawFirmService.createLawFirm(createLawFirmDto);
    }
}
