package com.paralex.erp.services;

import com.google.firebase.auth.FirebaseAuthException;
import com.paralex.erp.dtos.*;
import com.paralex.erp.entities.LawyerProfileEntity;
import com.paralex.erp.entities.UserEntity;
import com.paralex.erp.repositories.LawyerProfileRepository;
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
public class LawyerProfileService {
    private final UserService userService;
    private final AuthorizationService authorizationService;
    private final LawyerProfileRepository lawyerProfileRepository;
    private final UserEntity userEntity;
    private List<AddAuthorizationRecordDto> defaultLawyerProfileAuthorizationRecords = List.of(AddAuthorizationRecordDto.builder()
            .build());

    // TODO find lawyer

    public void enableProfile(@NotNull EnableProfileDto enableProfileDto) {
        final var userId = userService.findUserByEmail(enableProfileDto.getUserEmail())
                .orElseThrow()
                .getId();

        lawyerProfileRepository.enableProfileByEmail(userId);
    }

    public void disableProfile(@NotNull EnableProfileDto enableProfileDto) {
        final var userId = userService.findUserByEmail(enableProfileDto.getUserEmail())
                .orElseThrow()
                .getId();

        lawyerProfileRepository.disableProfileByEmail(userId);
    }

    public void getProfiles(@NotNull DateTimePaginatedRequestDto dateTimePaginatedRequestDto) {
        final var pageSize = dateTimePaginatedRequestDto.getPageSize();
        final var pageNumber = dateTimePaginatedRequestDto.getPageNumber();
        final var startDate = dateTimePaginatedRequestDto.getStart();
        final var endDate = dateTimePaginatedRequestDto.getEnd();

        final Specification<LawyerProfileEntity> specification = (root, query, cb) -> cb.between(root.get("time"), startDate, endDate);
        final var pageable = PageRequest.of(pageNumber, pageSize, Sort.by("time").descending());

        lawyerProfileRepository.findAll(specification, pageable);
    }

    public void createProfile(@NotNull CreateLawyerProfileDto createLawyerProfileDto) throws IOException, FirebaseAuthException {
        final var userId = userService.createUserProfile(createLawyerProfileDto);
        final boolean defaultLawyerProfileStatus = false;

        final var example = Example.of(LawyerProfileEntity.builder()
                .userId(userId)
                .build(), ExampleMatcher.matchingAll()
                .withIgnoreNullValues()
                .withIgnorePaths("id", "time"));

        final var lawyerProfile = lawyerProfileRepository.findOne(example);

        if (lawyerProfile.isPresent())
            return;

        lawyerProfileRepository.save(LawyerProfileEntity.builder()
                .userId(userId)
                .supremeCourtNumber(createLawyerProfileDto.getSupremeCourtNumber())
                .creatorId(userEntity.getId())
                .status(defaultLawyerProfileStatus)
                .build());

        authorizationService.addAuthorizationRecord(defaultLawyerProfileAuthorizationRecords.stream()
                .peek(addAuthorizationRecordDto -> addAuthorizationRecordDto.setPrincipal(userId))
                .toList());
    }
}
