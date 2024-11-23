package com.paralex.erp.services;

import com.google.firebase.auth.FirebaseAuthException;
import com.paralex.erp.dtos.AddAuthorizationRecordDto;
import com.paralex.erp.dtos.CreateDriverProfileDto;
import com.paralex.erp.dtos.DateTimePaginatedRequestDto;
import com.paralex.erp.dtos.EnableProfileDto;
import com.paralex.erp.entities.DriverProfileEntity;
import com.paralex.erp.entities.UserEntity;
import com.paralex.erp.repositories.DriverProfileRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Validated
@Service
@Log4j2
public class DriverProfileService {
    private final UserService userService;
    private final AuthorizationService authorizationService;
    private final DriverProfileRepository driverProfileRepository;
    private final UserEntity userEntity;
    private List<AddAuthorizationRecordDto> defaultDriverProfileAuthorizationRecords = List.of(AddAuthorizationRecordDto.builder()
            .build());

    // TODO find driver profile
    public void enableProfile(@NotNull EnableProfileDto enableProfileDto) {
        final var userId = userService.findUserByEmail(enableProfileDto.getUserEmail())
                .orElseThrow()
                .getId();

        driverProfileRepository.enableProfileByEmail(userId);
    }

    public void disableProfile(@NotNull EnableProfileDto enableProfileDto) {
        final var userId = userService.findUserByEmail(enableProfileDto.getUserEmail())
                .orElseThrow()
                .getId();

        driverProfileRepository.disableProfileByEmail(userId);
    }

    public void getProfiles(@NotNull DateTimePaginatedRequestDto dateTimePaginatedRequestDto) {
        final var pageSize = dateTimePaginatedRequestDto.getPageSize();
        final var pageNumber = dateTimePaginatedRequestDto.getPageNumber();
        final var startDate = dateTimePaginatedRequestDto.getStart();
        final var endDate = dateTimePaginatedRequestDto.getEnd();

        final Specification<DriverProfileEntity> specification = (root, query, cb) -> cb.between(root.get("time"), startDate, endDate);
        final var pageable = PageRequest.of(pageNumber, pageSize, Sort.by("time").descending());

        driverProfileRepository.findAll(specification, pageable);
    }

    public void createProfile(@NotNull CreateDriverProfileDto createDriverProfileDto) throws IOException, FirebaseAuthException {
        final var userId = userService.createUserProfile(createDriverProfileDto);
        final boolean defaultDriverProfileStatus = false;

        final var example = Example.of(DriverProfileEntity.builder()
                .userId(userId)
                .build(), ExampleMatcher.matchingAll().withIgnoreNullValues()
                .withIgnorePaths("id", "time"));
        final var driverProfile = driverProfileRepository.findOne(example);

        if (driverProfile.isPresent())
            return;

        driverProfileRepository.save(DriverProfileEntity.builder()
                .userId(userId)
                .status(defaultDriverProfileStatus)
                .creatorId(userEntity.getId())
                .build());

        authorizationService.addAuthorizationRecord(defaultDriverProfileAuthorizationRecords.stream()
                .peek(addAuthorizationRecordDto -> addAuthorizationRecordDto.setPrincipal(userId))
                .toList());
    }
}
