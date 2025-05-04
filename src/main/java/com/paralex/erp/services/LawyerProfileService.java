package com.paralex.erp.services;

import com.google.firebase.auth.FirebaseAuthException;
import com.paralex.erp.commons.utils.Helper;
import com.paralex.erp.dtos.*;
import com.paralex.erp.entities.*;
import com.paralex.erp.enums.RegistrationLevel;
import com.paralex.erp.enums.UserType;
import com.paralex.erp.exceptions.AlreadyExistException;
import com.paralex.erp.exceptions.ErrorException;
import com.paralex.erp.repositories.LawyerPracticeAreaRepository;
import com.paralex.erp.repositories.LawyerProfileRepository;
import com.paralex.erp.repositories.LawyerReviewRepository;
import com.paralex.erp.repositories.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Validated
@Service
@Log4j2
public class LawyerProfileService {
    @Autowired
    private RestTemplate restTemplate;

    @Value("${server.url}")
    private String baseUrl;
    private final UserService userService;
    private final AuthorizationService authorizationService;
    private final LawyerProfileRepository lawyerProfileRepository;
    private final LawyerPracticeAreaRepository lawyerPracticeAreaRepository;
    private final EntityManager entityManager;
    private final UserEntity userEntity;
    private final List<AddAuthorizationRecordDto> defaultLawyerProfileAuthorizationRecords = List.of(AddAuthorizationRecordDto.builder()
            .build());
    private final MongoTemplate mongoTemplate;
    private final PaymentGatewayService paymentGatewayService;
    private final UserRepository userRepository;
    private final WalletService walletService;
    private final NotificationService notificationService;
    @Autowired
    private Helper helper;
    @Autowired
    private EmailService emailService;
    @Autowired
    private LawyerReviewRepository lawyerReviewRepository;

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
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated");
        }

        var userEmail = auth.getName();
        var userEntity = userService.findUserByEmail(userEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));
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

//    public List<LawyerProfileEntity> getProfiles(@NotNull DateTimePaginatedRequestDto dateTimePaginatedRequestDto) throws IOException {
//        final var pageSize = dateTimePaginatedRequestDto.getPageSize();
//        final var pageNumber = dateTimePaginatedRequestDto.getPageNumber();
//        final var startDate = dateTimePaginatedRequestDto.getStartDate();
//        final var endDate = dateTimePaginatedRequestDto.getEndDate();
//
//        // Create MongoDB Criteria for date range
//        Criteria dateCriteria = Criteria.where("time").gte(startDate).lte(endDate);
//
//        // Create the query
//        Query query = new Query(dateCriteria)
//                .with(PageRequest.of(pageNumber, pageSize, Sort.by("time").descending()));
//
//        // Execute query with MongoTemplate
//        List<LawyerProfileEntity> profiles = mongoTemplate.find(query, LawyerProfileEntity.class);
//
//        return profiles;
//    }

    public List<LawyerProfileEntity> getProfiles(@NotNull PaginatedRequestDto paginatedRequestDto) throws IOException {
        final var pageSize = paginatedRequestDto.getPageSize();
        final var pageNumber = paginatedRequestDto.getPageNumber();

        // Create a query without date criteria
        Query query = new Query()
                .with(PageRequest.of(pageNumber, pageSize, Sort.by("time").descending()));

        // Execute query with MongoTemplate
        List<LawyerProfileEntity> profiles = mongoTemplate.find(query, LawyerProfileEntity.class);

        return profiles;
    }


    @Transactional
    public GlobalResponse<?> createProfile(CreateLawyerProfileDto createLawyerProfileDto) throws Exception {
//        final var userId = userService.createUserProfile(createLawyerProfileDto);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated");
        }

        var userEmail = auth.getName();
        var userEntity = userService.findUserByEmail(userEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));
        final boolean defaultLawyerProfileStatus = false;

        // MongoDB query to check if the profile already exists
        Optional<LawyerProfileEntity> lawyerProfile = lawyerProfileRepository.findByUserId(userEntity.getId());

        if (lawyerProfile.isPresent()) {
            throw new AlreadyExistException("Lawyer Profile already exists");
        }

        // Map practiceAreas from List<String> to List<LawyerPracticeAreaEntity>
        List<String> practiceAreaEntities = new ArrayList<>(createLawyerProfileDto.getPracticeAreas());


        // Create and save the new LawyerProfileEntity
        final var savedLawyerProfile = lawyerProfileRepository.save(LawyerProfileEntity.builder()
                .state(createLawyerProfileDto.getStateOfPractice())
                .supremeCourtNumber(createLawyerProfileDto.getSupremeCourtNumber())
                .location(new Point(createLawyerProfileDto.getLatitude(), createLawyerProfileDto.getLongitude())) // Assuming location is a Point
                        .photoUrl(createLawyerProfileDto.getPhotoUrl())
                        .creator(userEntity)
                        .user(userEntity)
                                .lawyerName(createLawyerProfileDto.getFirstName() + " " + createLawyerProfileDto.getLastName())
                        .practiceAreas(practiceAreaEntities)
                        .NBABranchAffiliation(createLawyerProfileDto.getNBABranchAffiliation())
                .userId(userEntity.getId())
                        .time(LocalDateTime.now())
                .creatorId(userEntity.getId())
                .status(defaultLawyerProfileStatus)
                .build());

        final var customerCode = paymentGatewayService.createPaystackCustomer(CreateCustomerDto.builder()
                .firstName(createLawyerProfileDto.getFirstName())
                .lastName(createLawyerProfileDto.getLastName())
                .email(createLawyerProfileDto.getEmail())
                .phoneNumber(createLawyerProfileDto.getPhoneNumber())
                .build());

        userEntity.setCustomerCode(customerCode);
        userEntity.setFirstName(createLawyerProfileDto.getFirstName());
        userEntity.setLastName(createLawyerProfileDto.getLastName());
        userEntity.setRegistrationLevel(RegistrationLevel.KYC_COMPLETED);
        userRepository.save(userEntity);

        String businessId = UUID.randomUUID().toString();

        CreateWalletDTO createWalletDTO =  new CreateWalletDTO();
        createWalletDTO.setName(createLawyerProfileDto.getFirstName() + " " + createLawyerProfileDto.getLastName());
        createWalletDTO.setBusinessId(businessId);
        createWalletDTO.setPhoneNumber(createLawyerProfileDto.getPhoneNumber());
        createWalletDTO.setEmail(createLawyerProfileDto.getEmail());
        createWalletDTO.setEmployeeId("business");
        createWalletDTO.setAccountType("business");

        // Call createWallet and extract the walletId
        Object walletResponse = walletService.createWallet(createWalletDTO);

        if (walletResponse instanceof OkResponse) {
            OkResponse<NewWallet> okResponse = (OkResponse<NewWallet>) walletResponse;
            NewWallet savedWallet = okResponse.getData();
            String walletId = savedWallet.getWalletId();
            String savedWalletBusinessId = savedWallet.getBusinessId();

            // Save the walletId to the customer entity
            userEntity.setWalletId(walletId);
            userEntity.setBusinessId(savedWalletBusinessId);
            userRepository.save(userEntity);

        } else if (walletResponse instanceof FailedResponse) {
            FailedResponse failedResponse = (FailedResponse) walletResponse;
            throw new ErrorException("Wallet creation failed: " + failedResponse.getDebugMessage());
        }

        // Create an admin notification after submitting bond request
        String notificationTitle = "New Lawyer Profile Created";
        String notificationMessage = "A lawyer profile has been created by " + userEntity.getName();

        String url = baseUrl + "admin/create-test-admin-notification"; // Controller endpoint URL

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("title", notificationTitle);
        params.add("message", notificationMessage);
        params.add("userId", "null");

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);

        try {
            // Make the HTTP call to the controller
            ResponseEntity<AdminNotification> response1 = restTemplate.exchange(url, HttpMethod.POST, requestEntity, AdminNotification.class);

            if (response1.getStatusCode() != HttpStatus.OK) {
                // Log the error but don't stop the flow
                String errorMessage = "Failed to create admin notification: " + response1.getStatusCode();
                System.err.println(errorMessage);  // You can log this error or handle as needed
            }
        } catch (Exception e) {
            // Catch any exception thrown by the HTTP request and log the error
            System.err.println("Error making HTTP call for admin notification: " + e.getMessage());
        }

        notificationService.broadcastNotification(notificationTitle, notificationMessage);

        // Save the practice areas
        lawyerPracticeAreaRepository.saveAll(createLawyerProfileDto.getPracticeAreas().stream()
                .map(item -> LawyerPracticeAreaEntity.builder()
                        .lawPracticeAreaId(item)
                        .lawyerProfileId(savedLawyerProfile.getId())
                        .creatorId(userEntity.getId())
                        .build())
                .toList());

        // Add authorization record
//        authorizationService.addAuthorizationRecord(defaultLawyerProfileAuthorizationRecords.stream()
//                .peek(addAuthorizationRecordDto -> addAuthorizationRecordDto.setPrincipal(userEntity.getId()))
//                .toList());
        GlobalResponse<String> response = new GlobalResponse<>();
        response.setStatus(HttpStatus.ACCEPTED);
        response.setMessage("Lawyer Profile Created Successfully.");
        return response;
    }

    @Transactional
    public GlobalResponse<?> createProfile(@NotNull CreateMyLawyerProfileDto createMyLawyerProfileDto) {
//        final var userId = userEntity.getId();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated");
        }

        var userEmail = auth.getName();
        var userEntity = userService.findUserByEmail(userEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));
        final boolean defaultLawyerProfileStatus = false;

        // MongoDB query to check if the profile already exists
        Optional<LawyerProfileEntity> lawyerProfile = lawyerProfileRepository.findByUserId(userEntity.getId());

        if (lawyerProfile.isPresent()) {
            throw new AlreadyExistException("Lawyer Profile already exists");
            // If the profile already exists, do nothing
        } else {

            // Create and save the new LawyerProfileEntity
            final var savedLawyerProfile = lawyerProfileRepository.save(LawyerProfileEntity.builder()
                    .state(createMyLawyerProfileDto.getStateOfPractice())
                    .supremeCourtNumber(createMyLawyerProfileDto.getSupremeCourtNumber())
                    .location(new Point(createMyLawyerProfileDto.getLatitude(), createMyLawyerProfileDto.getLongitude())) // Assuming location is a Point
                    .userId(userEntity.getId())
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
//        authorizationService.addAuthorizationRecord(defaultLawyerProfileAuthorizationRecords.stream()
//                .peek(addAuthorizationRecordDto -> addAuthorizationRecordDto.setPrincipal(userEntity.getId()))
//                .toList());
            GlobalResponse<String> response = new GlobalResponse<>();
            response.setStatus(HttpStatus.ACCEPTED);
            response.setMessage("Lawyer Profile Created Successfully.");
            return response;
        }
    }

    public LawyerProfileEntity getLawyerProfileByUserId(String userId) {
        return lawyerProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lawyer profile not found for this user ID"));
    }


    @Transactional
    public GlobalResponse<?> adminCreateLawyerProfile(CreateLawyerProfileDto dto) throws Exception {
        // Authenticate admin
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Admin not authenticated");
        }

        var adminEmail = auth.getName();
        var adminUser = userService.findUserByEmail(adminEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Admin not found"));

        // Check if user with email already exists
        Optional<UserEntity> optionalLawyer = userService.findUserByEmail(dto.getEmail());
        UserEntity lawyerUser;
        String defaultPassword = UUID.randomUUID().toString().substring(0, 8); // Generate temp password

        if (optionalLawyer.isEmpty()) {
            // Register new user without OTP
            lawyerUser = new UserEntity();
            lawyerUser.setEmail(dto.getEmail());
            lawyerUser.setFirstName(dto.getFirstName());
            lawyerUser.setLastName(dto.getLastName());
            lawyerUser.setPhoneNumber(dto.getPhoneNumber());
            lawyerUser.setTime(LocalDateTime.now());
            lawyerUser.setUserType(UserType.SERVICE_PROVIDER_LAWYER);
            lawyerUser.setRegistrationLevel(RegistrationLevel.KYC_COMPLETED);
            lawyerUser.setEnabled(true);
            lawyerUser.setPassword(helper.encodePassword(defaultPassword));

            lawyerUser = userRepository.save(lawyerUser);
        } else {
            throw new AlreadyExistException("User with email already exists");
        }

        // Check if profile already exists
        if (lawyerProfileRepository.findByUserId(lawyerUser.getId()).isPresent()) {
            throw new AlreadyExistException("Lawyer profile already exists");
        }

        // Create and save lawyer profile
        var lawyerProfile = LawyerProfileEntity.builder()
                .state(dto.getStateOfPractice())
                .supremeCourtNumber(dto.getSupremeCourtNumber())
                .location(new Point(dto.getLatitude(), dto.getLongitude()))
                .practiceAreas(dto.getPracticeAreas())
                .NBABranchAffiliation(dto.getNBABranchAffiliation())
                .userId(lawyerUser.getId())
                .user(lawyerUser)
                .creator(adminUser)
                .creatorId(adminUser.getId())
                .lawyerName(dto.getFirstName() + " " + dto.getLastName())
                .status(false)
                .time(LocalDateTime.now())
                .build();

        lawyerProfile = lawyerProfileRepository.save(lawyerProfile);

        // Create Paystack customer
        final var customerCode = paymentGatewayService.createPaystackCustomer(CreateCustomerDto.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .phoneNumber(dto.getPhoneNumber())
                .build());
        lawyerUser.setCustomerCode(customerCode);

        // Create wallet
        String businessId = UUID.randomUUID().toString();
        CreateWalletDTO createWalletDTO =  new CreateWalletDTO();
        createWalletDTO.setName(dto.getFirstName() + " " + dto.getLastName());
        createWalletDTO.setBusinessId(businessId);
        createWalletDTO.setPhoneNumber(dto.getPhoneNumber());
        createWalletDTO.setEmail(dto.getEmail());
        createWalletDTO.setEmployeeId("business");
        createWalletDTO.setAccountType("business");

        Object walletResponse = walletService.createWallet(createWalletDTO);

        if (walletResponse instanceof OkResponse) {
            var wallet = ((OkResponse<NewWallet>) walletResponse).getData();
            lawyerUser.setWalletId(wallet.getWalletId());
            lawyerUser.setBusinessId(wallet.getBusinessId());
        } else {
            throw new ErrorException("Wallet creation failed.");
        }

        // Save updated user
        userRepository.save(lawyerUser);

        // Save lawyer practice areas
        LawyerProfileEntity finalLawyerProfile = lawyerProfile;
        lawyerPracticeAreaRepository.saveAll(dto.getPracticeAreas().stream()
                .map(pa -> LawyerPracticeAreaEntity.builder()
                        .lawPracticeAreaId(pa)
                        .lawyerProfileId(finalLawyerProfile.getId())
                        .creatorId(adminUser.getId())
                        .build())
                .toList());

        // Send email with login credentials
        emailService.sendLawyerWelcomeEmail(dto.getEmail(), dto.getFirstName(), dto.getLastName(), defaultPassword);

        // Broadcast admin notification
        String title = "Lawyer Profile Created";
        String msg = "Profile created for lawyer: " + dto.getFirstName() + " " + dto.getLastName();
        notificationService.broadcastNotification(title, msg);

        GlobalResponse<String> response = new GlobalResponse<>();
        response.setStatus(HttpStatus.ACCEPTED);
        response.setMessage("Lawyer Profile Created Successfully.");
        return response;
    }

    public void submitReview(String lawyerProfileId, String reviewerId, int rating, String comment) {

        if (rating < 1 || rating > 5) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Rating must be between 1 and 5");
        }

        LawyerReviewEntity review = LawyerReviewEntity.builder()
                .lawyerProfileId(lawyerProfileId)
                .reviewerId(reviewerId)
                .rating(rating)
                .comment(comment)
                .reviewDate(LocalDateTime.now())
                .build();

        lawyerReviewRepository.save(review);

        // Recalculate and update aggregate rating (optional)
        var reviews = lawyerReviewRepository.findByLawyerProfileId(lawyerProfileId);
        double average = reviews.stream().mapToInt(LawyerReviewEntity::getRating).average().orElse(0.0);
        int total = reviews.size();

        var lawyer = lawyerProfileRepository.findById(lawyerProfileId)
                .orElseThrow(() -> new ErrorException("Lawyer not found"));
        lawyer.setAverageRating(average);
        lawyer.setTotalReviews(total);
        lawyerProfileRepository.save(lawyer);
    }



    public List<ReviewDto> getReviewsForLawyer(String lawyerId) {
        return lawyerReviewRepository.findByLawyerProfileId(lawyerId)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private ReviewDto convertToDto(LawyerReviewEntity entity) {
        return ReviewDto.builder()
                .lawyerId(entity.getId())
                .reviewerId(entity.getReviewerId())
                .rating(entity.getRating())
                .comment(entity.getComment())
                .timestamp(entity.getReviewDate())
                .build();
    }

}

