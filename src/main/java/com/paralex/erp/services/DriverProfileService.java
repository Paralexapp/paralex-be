package com.paralex.erp.services;

import com.google.firebase.auth.FirebaseAuthException;
import com.paralex.erp.dtos.*;
import com.paralex.erp.entities.DriverProfileEntity;
import com.paralex.erp.entities.UserEntity;
import com.paralex.erp.repositories.DriverProfileRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
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
public class DriverProfileService {
    public static final String DRIVER_PROFILE_ID = "driverProfileId";
    private final DriverProfileRepository driverProfileRepository;
    private final EntityManager entityManager;
    private final UserEntity userEntity;
    private final UserService userService;
    private final AuthorizationService authorizationService;

    private List<AddAuthorizationRecordDto> defaultDriverProfileAuthorizationRecords = List.of(AddAuthorizationRecordDto.builder()
            .build());

    public Long countAllOrBetweenTime(CountDto countDto) {
        final LocalDateTime startDate = countDto.getStartDate();
        final LocalDateTime endDate = countDto.getEndDate();
        final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        final Root<DriverProfileEntity> root = criteriaQuery.from(DriverProfileEntity.class);
        final List<Predicate> predicateList = new ArrayList<>();

        criteriaQuery.select(criteriaBuilder.count(root));

        if (startDate != null && endDate != null)
            predicateList.add(criteriaBuilder.between(root.get("time"), startDate, endDate));

        return entityManager
                .createQuery(criteriaQuery.where(predicateList.toArray(new Predicate[0])))
                .getSingleResult();
    }

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

    public DriverProfileEntity getMyProfile() {
        return driverProfileRepository.findById(userEntity.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public List<DriverProfileEntity> getProfiles(@NotNull DateTimePaginatedRequestDto dateTimePaginatedRequestDto) {
        final var pageSize = dateTimePaginatedRequestDto.getPageSize();
        final var pageNumber = dateTimePaginatedRequestDto.getPageNumber();
        final var startDate = dateTimePaginatedRequestDto.getStartDate();
        final var endDate = dateTimePaginatedRequestDto.getEndDate();

        log.info("start -> {} | end -> {}", startDate, endDate);

        final Specification<DriverProfileEntity> specification = (root, query, cb) -> cb.between(root.get("time"), startDate, endDate);
        final var pageable = PageRequest.of(pageNumber, pageSize, Sort.by("time").descending());

        return driverProfileRepository.findAll(specification, pageable)
                .getContent();
    }

    public DriverProfileEntity findDriverProfileBy(@NotNull FindDriverProfileDto findDriverProfileDto) {
        // Find the user based on email and phone number
        Optional<UserEntity> user = Optional.ofNullable(userService.findUserBy(FindUserProfileDto.builder()
                .email(findDriverProfileDto.getEmail())
                .phoneNumber(findDriverProfileDto.getPhoneNumber())
                .build()));

        // If user is not found, throw exception
        UserEntity foundUser = user.orElseThrow(() -> {
            log.error("User not found with email: {} and phone number: {}",
                    findDriverProfileDto.getEmail(), findDriverProfileDto.getPhoneNumber());
            return new ResponseStatusException(HttpStatus.NOT_FOUND, "Driver not found");
        });

        // Retrieve userId and log it
        String userId = foundUser.getId();
        log.info("Found user ID: {}", userId);

        // Find the driver profile using userId
        return driverProfileRepository.findByUserId(userId)
                .orElseThrow(() -> {
                    log.error("Driver profile not found for userId: {}", userId);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Driver profile not found");
                });
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
                .hasRiderCard(createDriverProfileDto.isHasRiderCard())
                .hasBike(createDriverProfileDto.isHasBike())
                .bikeType(createDriverProfileDto.getBikeType())
                .bikeCapacity(createDriverProfileDto.getBikeCapacity())
                .chassisNumber(createDriverProfileDto.getChassisNumber())
                .guarantorClass(createDriverProfileDto.getGuarantorClass())
                .guarantorPhoneNumber(createDriverProfileDto.getGuarantorPhoneNumber())
                .guarantorEmail(createDriverProfileDto.getGuarantorEmail())
                .guarantorStateOfResidence(createDriverProfileDto.getGuarantorStateOfResidence())
                .guarantorResidentialAddress(createDriverProfileDto.getGuarantorResidentialAddress())
                .bvn(createDriverProfileDto.getBvn())
                .nin(createDriverProfileDto.getNin())
                .bankCode(createDriverProfileDto.getBankCode())
                .bankName(createDriverProfileDto.getBankName())
                .accountNumber(createDriverProfileDto.getAccountNumber())
                .accountName(createDriverProfileDto.getAccountName())
                .passportUrl(createDriverProfileDto.getPassportUrl())
                .userId(userId)
                .status(defaultDriverProfileStatus)
                .creatorId(userEntity.getId())
                .build());

        authorizationService.addAuthorizationRecord(defaultDriverProfileAuthorizationRecords.stream()
                .peek(addAuthorizationRecordDto -> addAuthorizationRecordDto.setPrincipal(userId))
                .toList());
    }

    public void createProfile(@NotNull CreateMyDriverProfileDto createMyDriverProfileDto) {
        final var userId = userEntity.getId();
        final boolean defaultDriverProfileStatus = false;

        final var example = Example.of(DriverProfileEntity.builder()
                .userId(userId)
                .build(), ExampleMatcher.matchingAll().withIgnoreNullValues()
                .withIgnorePaths("id", "time"));
        final var driverProfile = driverProfileRepository.findOne(example);

        if (driverProfile.isPresent())
            return;

        driverProfileRepository.save(DriverProfileEntity.builder()
                .hasRiderCard(createMyDriverProfileDto.isHasRiderCard())
                .hasBike(createMyDriverProfileDto.isHasBike())
                .bikeType(createMyDriverProfileDto.getBikeType())
                .bikeCapacity(createMyDriverProfileDto.getBikeCapacity())
                .chassisNumber(createMyDriverProfileDto.getChassisNumber())
                .guarantorClass(createMyDriverProfileDto.getGuarantorClass())
                .guarantorPhoneNumber(createMyDriverProfileDto.getGuarantorPhoneNumber())
                .guarantorEmail(createMyDriverProfileDto.getGuarantorEmail())
                .guarantorStateOfResidence(createMyDriverProfileDto.getGuarantorStateOfResidence())
                .guarantorResidentialAddress(createMyDriverProfileDto.getGuarantorResidentialAddress())
                .bvn(createMyDriverProfileDto.getBvn())
                .nin(createMyDriverProfileDto.getNin())
                .bankCode(createMyDriverProfileDto.getBankCode())
                .bankName(createMyDriverProfileDto.getBankName())
                .accountNumber(createMyDriverProfileDto.getAccountNumber())
                .accountName(createMyDriverProfileDto.getAccountName())
                .passportUrl(createMyDriverProfileDto.getPassportUrl())
                .userId(userId)
                .status(defaultDriverProfileStatus)
                .creatorId(userId)
                .build());

        authorizationService.addAuthorizationRecord(defaultDriverProfileAuthorizationRecords.stream()
                .peek(addAuthorizationRecordDto -> addAuthorizationRecordDto.setPrincipal(userId))
                .toList());
    }

    public DriverProfileEntity findDriverProfileById(@NotNull String id) {
        return driverProfileRepository.findById(id)
                .orElseThrow();
    }
}
