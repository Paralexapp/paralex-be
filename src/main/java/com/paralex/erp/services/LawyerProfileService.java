package com.paralex.erp.services;

import com.google.firebase.auth.FirebaseAuthException;
import com.paralex.erp.dtos.*;
import com.paralex.erp.entities.LawyerPracticeAreaEntity;
import com.paralex.erp.entities.LawyerProfileEntity;
import com.paralex.erp.entities.UserEntity;
import com.paralex.erp.repositories.LawyerPracticeAreaRepository;
import com.paralex.erp.repositories.LawyerProfileRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.geo.Point;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Validated
@Service
@Log4j2
public class LawyerProfileService {
    private final UserService userService;
    private final AuthorizationService authorizationService;
    private final LawyerProfileRepository lawyerProfileRepository;
    private final LawyerPracticeAreaRepository lawyerPracticeAreaRepository;
    private final EntityManager entityManager;
    private final UserEntity userEntity;
    private final List<AddAuthorizationRecordDto> defaultLawyerProfileAuthorizationRecords = List.of(AddAuthorizationRecordDto.builder()
            .build());

    public Long countAllOrBetweenTime(CountDto countDto) {
        final LocalDateTime startDate = countDto.getStartDate();
        final LocalDateTime endDate = countDto.getEndDate();
        final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        final Root<LawyerProfileEntity> root = criteriaQuery.from(LawyerProfileEntity.class);
        final List<Predicate> predicateList = new ArrayList<>();

        criteriaQuery.select(criteriaBuilder.count(root));

        if (startDate != null && endDate != null)
            predicateList.add(criteriaBuilder.between(root.get("time"), startDate, endDate));

        return entityManager
                .createQuery(criteriaQuery.where(predicateList.toArray(new Predicate[0])))
                .getSingleResult();
    }

    public List<LawyerProfileEntity> findLawyersAroundLocation(@NotNull FindLawyersNearLocation findLawyersNearLocation) {
        return lawyerProfileRepository.searchLawyers(
                findLawyersNearLocation.getLatitude(),
                findLawyersNearLocation.getLongitude(),
                findLawyersNearLocation.getPageSize(),
                findLawyersNearLocation.getPageNumber() * findLawyersNearLocation.getPageSize());
    }

    public void deleteLawyerPracticeArea(@NotNull String id) {
        lawyerPracticeAreaRepository.deleteById(id);
    }

    public void addLawyerPracticeAreas(@NotNull @NotEmpty List<AddLawyerPracticeAreaDto> addLawyerPracticeAreaDtoList) {
        final var lawyerProfile = findMyProfile();

        lawyerPracticeAreaRepository.saveAll(addLawyerPracticeAreaDtoList.stream()
                .map(item -> LawyerPracticeAreaEntity.builder()
                        .lawPracticeAreaId(item.getLawPracticeAreaId())
                        .lawyerProfileId(lawyerProfile.getId())
                        .creatorId(userEntity.getId())
                        .build())
                .toList());
    }

    // TODO find lawyer by code
    public LawyerProfileEntity findLawyerProfileBy(@NotNull FindLawyerProfileDto findLawyerProfileDto) {
        // Fetch user by email and phone number
        Optional<UserEntity> user = Optional.ofNullable(userService.findUserBy(FindUserProfileDto.builder()
                .email(findLawyerProfileDto.getEmail())
                .phoneNumber(findLawyerProfileDto.getPhoneNumber())
                .build()));

        // Handle case where user is not found
        UserEntity foundUser = user.orElseThrow(() -> {
            log.error("User not found with email: {} and phone number: {}",
                    findLawyerProfileDto.getEmail(),
                    findLawyerProfileDto.getPhoneNumber());
            return new ResponseStatusException(HttpStatus.NOT_FOUND, "Lawyer not found");
        });

        // Log retrieved user ID
        String userId = foundUser.getId();
        log.info("Found user ID for lawyer lookup: {}", userId);

        // Fetch lawyer profile by user ID
        return lawyerProfileRepository.findByUserId(userId)
                .orElseThrow(() -> {
                    log.error("Lawyer profile not found for userId: {}", userId);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Lawyer profile not found");
                });
    }


    public LawyerProfileEntity findMyProfile() {
        return lawyerProfileRepository.findByUserId(userEntity.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public void enableProfile(@NotNull EnableProfileDto enableProfileDto) {
        final var userId = userService.findUserByEmail(enableProfileDto.getUserEmail())
                .orElseThrow()
                .getId();

        lawyerProfileRepository.disableProfileByUserId(userId);
    }

    public void disableProfile(@NotNull EnableProfileDto enableProfileDto) {
        final var userId = userService.findUserByEmail(enableProfileDto.getUserEmail())
                .orElseThrow()
                .getId();

        lawyerProfileRepository.enableProfileByUserId(userId);
    }

    public List<LawyerProfileEntity> getProfiles(@NotNull DateTimePaginatedRequestDto dateTimePaginatedRequestDto) throws IOException {
        final var pageSize = dateTimePaginatedRequestDto.getPageSize();
        final var pageNumber = dateTimePaginatedRequestDto.getPageNumber();
        final var startDate = dateTimePaginatedRequestDto.getStartDate();
        final var endDate = dateTimePaginatedRequestDto.getEndDate();

        final Specification<LawyerProfileEntity> specification = (root, query, cb) -> cb.between(root.get("time"), startDate, endDate);
        final var pageable = PageRequest.of(pageNumber, pageSize, Sort.by("time").descending());

        return (List<LawyerProfileEntity>) lawyerProfileRepository.findAll((Criteria) specification, pageable)
                .getContent();
    }

    @Transactional
    public void createProfile(@NotNull CreateLawyerProfileDto createLawyerProfileDto) throws IOException, FirebaseAuthException {
        final var userId = userService.createUserProfile(createLawyerProfileDto);
        final boolean defaultLawyerProfileStatus = false;

        // MongoDB query to check if the profile already exists
        Optional<LawyerProfileEntity> lawyerProfile = lawyerProfileRepository.findByUserId(userId);

        if (lawyerProfile.isPresent()) {
            return; // If the profile already exists, do nothing
        }

        // Create and save the new LawyerProfileEntity
        final var savedLawyerProfile = lawyerProfileRepository.save(LawyerProfileEntity.builder()
                .state(createLawyerProfileDto.getStateOfPractice())
                .supremeCourtNumber(createLawyerProfileDto.getSupremeCourtNumber())
                .location(new Point(createLawyerProfileDto.getLatitude(), createLawyerProfileDto.getLongitude())) // Assuming location is a Point
                .userId(userId)
                .creatorId(userEntity.getId())
                .status(defaultLawyerProfileStatus)
                .build());

        // Save the practice areas
        lawyerPracticeAreaRepository.saveAll(createLawyerProfileDto.getPracticeAreas().stream()
                .map(item -> LawyerPracticeAreaEntity.builder()
                        .lawPracticeAreaId(item)
                        .lawyerProfileId(savedLawyerProfile.getId())
                        .creatorId(userEntity.getId())
                        .build())
                .toList());

        // Add authorization record
        authorizationService.addAuthorizationRecord(defaultLawyerProfileAuthorizationRecords.stream()
                .peek(addAuthorizationRecordDto -> addAuthorizationRecordDto.setPrincipal(userId))
                .toList());
    }

    @Transactional
    public void createProfile(@NotNull CreateMyLawyerProfileDto createMyLawyerProfileDto) {
        final var userId = userEntity.getId();
        final boolean defaultLawyerProfileStatus = false;

        // MongoDB query to check if the profile already exists
        Optional<LawyerProfileEntity> lawyerProfile = lawyerProfileRepository.findByUserId(userId);

        if (lawyerProfile.isPresent()) {
            return; // If the profile already exists, do nothing
        }

        // Create and save the new LawyerProfileEntity
        final var savedLawyerProfile = lawyerProfileRepository.save(LawyerProfileEntity.builder()
                .state(createMyLawyerProfileDto.getStateOfPractice())
                .supremeCourtNumber(createMyLawyerProfileDto.getSupremeCourtNumber())
                .location(new Point(createMyLawyerProfileDto.getLatitude(), createMyLawyerProfileDto.getLongitude())) // Assuming location is a Point
                .userId(userId)
                .creatorId(userEntity.getId())
                .status(defaultLawyerProfileStatus)
                .build());

        // Save the practice areas
        lawyerPracticeAreaRepository.saveAll(createMyLawyerProfileDto.getPracticeAreas().stream()
                .map(item -> LawyerPracticeAreaEntity.builder()
                        .lawPracticeAreaId(item)
                        .lawyerProfileId(savedLawyerProfile.getId())
                        .creatorId(userEntity.getId())
                        .build())
                .toList());

        // Add authorization record
        authorizationService.addAuthorizationRecord(defaultLawyerProfileAuthorizationRecords.stream()
                .peek(addAuthorizationRecordDto -> addAuthorizationRecordDto.setPrincipal(userId))
                .toList());
    }
    }

