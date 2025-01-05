package com.paralex.erp.services;

import com.google.firebase.auth.FirebaseAuthException;
import com.paralex.erp.dtos.*;
import com.paralex.erp.entities.DriverProfileEntity;
import com.paralex.erp.entities.NewWallet;
import com.paralex.erp.entities.UserEntity;
import com.paralex.erp.enums.RegistrationLevel;
import com.paralex.erp.exceptions.AlreadyExistException;
import com.paralex.erp.exceptions.ErrorException;
import com.paralex.erp.repositories.DriverProfileRepository;
import com.paralex.erp.repositories.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private PaymentGatewayService paymentGatewayService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private WalletService walletService;

    // Enable profile by UserId
    public void enableProfileByUserId(String userId) {
        mongoTemplate.updateFirst(
                new Query(Criteria.where("userId").is(userId)),
                new Update().set("status", true),  // Set status to true
                DriverProfileEntity.class
        );
    }

    // Disable profile by UserId
    public void disableProfileByUserId(String userId) {
        mongoTemplate.updateFirst(
                new Query(Criteria.where("userId").is(userId)),
                new Update().set("status", false),  // Set status to false
                DriverProfileEntity.class
        );
    }

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

        this.enableProfileByUserId(userId);
    }

    public void disableProfile(@NotNull EnableProfileDto enableProfileDto) {
        final var userId = userService.findUserByEmail(enableProfileDto.getUserEmail())
                .orElseThrow()
                .getId();

        this.disableProfileByUserId(userId);
    }

    public DriverProfileEntity getMyProfile() {
        return driverProfileRepository.findById(userEntity.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public List<DriverProfileEntity> getProfiles(DateTimePaginatedRequestDto dateTimePaginatedRequestDto) throws IOException {
        final var pageSize = dateTimePaginatedRequestDto.getPageSize();
        final var pageNumber = dateTimePaginatedRequestDto.getPageNumber();
        final var startDate = dateTimePaginatedRequestDto.getStartDate();
        final var endDate = dateTimePaginatedRequestDto.getEndDate();

        log.info("start -> {} | end -> {}", startDate, endDate);

        // Build query using Criteria API
        Criteria criteria = Criteria.where("time").gte(startDate).lte(endDate);

        Query query = new Query(criteria)
                .with(PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "time")));

        // Execute query with MongoTemplate
        List<DriverProfileEntity> profiles = mongoTemplate.find(query, DriverProfileEntity.class);

        log.info("Retrieved {} profiles.", profiles.size());
        return profiles;
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


    @Transactional
    public GlobalResponse<?> createProfile(CreateDriverProfileDto createDriverProfileDto) throws Exception {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated");
        }

        var userEmail = auth.getName();
        var userEntity = userService.findUserByEmail(userEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));

//        final var userId = userService.createUserProfile(createDriverProfileDto);
        final boolean defaultDriverProfileStatus = false;

//        final var example = Example.of(DriverProfileEntity.builder()
//                .userId(userEntity.getId())
//                .build(), ExampleMatcher.matchingAll().withIgnoreNullValues()
//                .withIgnorePaths("id", "time"));
        final var driverProfile = driverProfileRepository.findById(userEntity.getId());

        if (driverProfile.isPresent())
            throw new AlreadyExistException("Driver Profile already exists");
        try {
            driverProfileRepository.save(DriverProfileEntity.builder()
                            .id(userEntity.getId())
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
                    .offline(false)
                    .nin(createDriverProfileDto.getNin())
                            .time(LocalDateTime.now())
                    .bankCode(createDriverProfileDto.getBankCode())
                    .bankName(createDriverProfileDto.getBankName())
                    .accountNumber(createDriverProfileDto.getAccountNumber())
                    .accountName(createDriverProfileDto.getAccountName())
                    .passportUrl(createDriverProfileDto.getPassportUrl())
                    .userId(userEntity.getId())
                    .status(defaultDriverProfileStatus)
                    .creatorId(userEntity.getId())
                    .build());
        } catch (Exception e) {
            log.error("Error saving driver profile: {}", e.getMessage(), e);
        }

        final var customerCode = paymentGatewayService.createPaystackCustomer(CreateCustomerDto.builder()
                .firstName(createDriverProfileDto.getFirstName())
                .lastName(createDriverProfileDto.getLastName())
                .email(createDriverProfileDto.getEmail())
                .phoneNumber(createDriverProfileDto.getPhoneNumber())
                .build());

        userEntity.setCustomerCode(customerCode);
        userEntity.setFirstName(createDriverProfileDto.getAccountName().split(" ")[0]);
        userEntity.setLastName(createDriverProfileDto.getAccountName().split(" ")[1]);
        userEntity.setRegistrationLevel(RegistrationLevel.KYC_COMPLETED);
        userRepository.save(userEntity);

        String businessId = UUID.randomUUID().toString();

        CreateWalletDTO createWalletDTO =  new CreateWalletDTO();
        createWalletDTO.setName(createDriverProfileDto.getAccountName());
        createWalletDTO.setBusinessId(businessId);
        createWalletDTO.setPhoneNumber(createDriverProfileDto.getPhoneNumber());
        createWalletDTO.setEmail(createDriverProfileDto.getEmail());
        createWalletDTO.setEmployeeId("business");
        createWalletDTO.setAccountType("business");

        // Call createWallet and extract the walletId
        Object walletResponse =walletService.createWallet(createWalletDTO);

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


//        authorizationService.addAuthorizationRecord(defaultDriverProfileAuthorizationRecords.stream()
//                .peek(addAuthorizationRecordDto -> addAuthorizationRecordDto.setPrincipal(userEntity.getId()))
//                .toList());
        GlobalResponse<String> response = new GlobalResponse<>();
        response.setStatus(HttpStatus.ACCEPTED);
        response.setMessage("Driver Profile Created Successfully.");
        return response;
    }

    public Optional<DriverProfileEntity> updateOfflineStatus(String driverId, boolean offlineStatus) {
        Optional<DriverProfileEntity> driverProfileOpt = driverProfileRepository.findById(driverId);

        if (driverProfileOpt.isPresent()) {
            DriverProfileEntity driverProfile = driverProfileOpt.get();
            driverProfile.setOffline(offlineStatus);
            driverProfileRepository.save(driverProfile);
            return Optional.of(driverProfile);
        }

        return Optional.empty();
    }

    public GlobalResponse<?> createProfile(@NotNull CreateMyDriverProfileDto createMyDriverProfileDto) {
//        final var userId = userEntity.getId();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated");
        }

        var userEmail = auth.getName();
        var userEntity = userService.findUserByEmail(userEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));
        final boolean defaultDriverProfileStatus = false;

        final var example = Example.of(DriverProfileEntity.builder()
                .userId(userEntity.getId())
                .build(), ExampleMatcher.matchingAll().withIgnoreNullValues()
                .withIgnorePaths("id", "time"));
        final var driverProfile = driverProfileRepository.findOne(example);

        if (driverProfile.isPresent()){
            throw new AlreadyExistException("Driver Profile already exists");
        }


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
                .userId(userEntity.getId())
                .status(defaultDriverProfileStatus)
                .creatorId(userEntity.getId())
                .build());

        GlobalResponse<String> response = new GlobalResponse<>();
        response.setStatus(HttpStatus.ACCEPTED);
        response.setMessage("Driver Profile Created Successfully.");
        return response;

//        authorizationService.addAuthorizationRecord(defaultDriverProfileAuthorizationRecords.stream()
//                .peek(addAuthorizationRecordDto -> addAuthorizationRecordDto.setPrincipal(userId))
//                .toList());
    }

    public DriverProfileEntity findDriverProfileById(@NotNull String id) {
        return driverProfileRepository.findById(id)
                .orElseThrow();
    }

    /**
     * Finds nearby drivers within a given radius of the provided location.
     *
     * @param latitude  the latitude of the location
     * @param longitude the longitude of the location
     * @param maxResults the maximum number of results to return
     * @return a list of nearby drivers
     */
    public List<NearbyDriverDto> findNearbyDrivers(double latitude, double longitude, int maxResults) {
        Point locationPoint = new Point(longitude, latitude);
        Distance maxDistance = new Distance(10, Metrics.KILOMETERS); // Search within 10 km radius

        // Query the database for drivers near the location
        List<DriverProfileEntity> nearbyDrivers = driverProfileRepository.findDriversNear(locationPoint, maxDistance, maxResults);

        // Filter out offline drivers and map the remaining drivers to DTOs
        return nearbyDrivers.stream()
                .filter(driver -> !driver.isOffline()) // Filter only drivers whose offlineStatus is false
                .map(driver -> NearbyDriverDto.builder()
                        .id(driver.getId())
                        .user(driver.getUser())
                        .distance(calculateDistance(latitude, longitude, driver.getLocation().getY(), driver.getLocation().getX())) // Use GeoJsonPoint location
                        .build())
                .toList();
    }


    public double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int EARTH_RADIUS = 6371; // Radius in kilometers
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS * c;
    }
}
