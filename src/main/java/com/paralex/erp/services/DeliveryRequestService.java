package com.paralex.erp.services;

import com.paralex.erp.documents.DeliveryRequestAssignmentDocument;
import com.paralex.erp.documents.DeliveryRequestDocument;
import com.paralex.erp.dtos.*;
import com.paralex.erp.entities.UserEntity;
import com.paralex.erp.repositories.DeliveryRequestAssignmentRepository;
import com.paralex.erp.repositories.DeliveryRequestRepository;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.RandomStringUtils;
import org.bson.Document;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
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
import java.time.ZoneOffset;
import java.util.List;
import java.util.Objects;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@RequiredArgsConstructor
@Validated
@Service
@Log4j2
public class DeliveryRequestService {
    public static final String DECLINED = "declined";
    public static final String ACCEPTED = "accepted";
    public static final String DELIVERY_REQUEST_ID1 = "deliveryRequestId";
    public static final String DELIVERY_REQUEST_ID = DELIVERY_REQUEST_ID1;
    public static final String DELIVERY_STAGES = "deliveryStages";
    public static final String CREATOR_ID1 = "creatorId";
    public static final String CREATOR_ID = CREATOR_ID1;
    public static final String DELIVERY_REQUESTS = "deliveryRequests";
    public static final String DELIVERY_STAGE_ID = "deliveryStageId";
    public static final String DRIVER_PROFILE_ID = "driverProfileId";
    public static final int EARTH_RADIUS = 6371;
    private final DeliveryRequestRepository deliveryRequestRepository;
    private final DeliveryRequestAssignmentRepository deliveryRequestAssignmentRepository;
    private final MongoTemplate mongoTemplate;
    private final DriverProfileService driverProfileService;
    private final DeliveryStageService deliveryStageService;
    private final WalletService walletService;
    private final LocationService locationService;
    private final UserEntity userEntity;
    private final UserService userService;

    // INFO it gets both the price and the distance
    public DeliveryRequestInformationDto getDeliveryDistanceInformation(@NotNull GetDeliveryRequestInformationDto getDeliveryRequestInformationDto) {
        final var pickup = getDeliveryRequestInformationDto.getPickup();
        final var destination = getDeliveryRequestInformationDto.getDestination();

        final var closestDeliveryLocation = locationService.findLocationNearestTo(destination.getLatitude(), destination.getLongitude())
                .orElseThrow();

        log.info("[location] nearest location found: name -> {}, latitude -> {}, longitude -> {}, amount -> {}",
                closestDeliveryLocation.getName(),
                closestDeliveryLocation.getLocation().getX(),
                closestDeliveryLocation.getLocation().getY(),
                closestDeliveryLocation.getAmount());

        final double distanceBetweenRequest = calculateDistanceBetweenPickupAndDestinationLocation(pickup, destination);
        // INFO Omo, I hope this does not truncate much. I think it should be fine
        final int amount = (int) Math.round(distanceBetweenRequest * closestDeliveryLocation.getAmount());

        return DeliveryRequestInformationDto.builder()
                .amount(amount)
                .distance(Math.round(distanceBetweenRequest))
                .build();
    }

    public List<DeliveryRequestDto> findDeliveryRequestByTrackingId(@NotNull String trackingId) {
        var where = Criteria.where("trackingId").is(trackingId);
        final var matchStage = match(where);
        final var deliveryStagesLookupStage = getDeliveryStageLookupStage();
        final var deliveryRequestAssignmentsLookupStage = getDeliveryRequestAssignmentsLookupStage();

        final TypedAggregation<DeliveryRequestDocument> aggregation = newAggregation(DeliveryRequestDocument.class,
                matchStage,
                deliveryStagesLookupStage,
                deliveryRequestAssignmentsLookupStage).withOptions(AggregationOptions.builder()
                .allowDiskUse(true)
                .build());

        return mongoTemplate.aggregate(aggregation, DeliveryRequestDto.class)
                .getMappedResults();
    }

    public List<DeliveryRequestAssignmentDto> getMyAssignedDeliveryRequests(@NotNull DateTimePaginatedRequestDto dateTimePaginatedRequestDto) {
        final var pageNumber = dateTimePaginatedRequestDto.getPageNumber();
        final var pageSize = dateTimePaginatedRequestDto.getPageSize();

        final var convertToTimeStage = addFields()
                .addField("time")
                .withValueOf(ConvertOperators.ToLong.toLong("time"))
                .build();
        final var matchStage = match(Criteria.where(CREATOR_ID)
                .is(userEntity.getId()));
        final var deliveryRequestsLookupStage = getDeliveryRequestLookupStage();
        final var addFieldStage = addFields().addField("deliveryRequest")
                .withValueOf(ArrayOperators.ArrayElemAt.arrayOf(DELIVERY_REQUESTS)
                        .elementAt(0))
                .build();
        final var sortStage = sort(Sort.by("time").descending());
        final var skipStage = skip(pageNumber.longValue() * pageSize.longValue());
        final var limitStage = limit(pageSize);

        final TypedAggregation<DeliveryRequestAssignmentDocument> aggregation = newAggregation(DeliveryRequestAssignmentDocument.class,
                convertToTimeStage,
                matchStage,
                deliveryRequestsLookupStage,
                addFieldStage,
                sortStage,
                skipStage,
                limitStage).withOptions(AggregationOptions.builder()
                .allowDiskUse(true)
                .build());

        return mongoTemplate.aggregate(aggregation, DeliveryRequestAssignmentDto.class)
                .getMappedResults();
    }

    public void setDeliveryRequestStageByDriver(@NotNull SetDeliveryRequestStageDto setDeliveryRequestStageDto) {
        final var deliveryStage = deliveryStageService.findDeliveryStageById(setDeliveryRequestStageDto.getDeliveryStageId())
                .orElseThrow();

        if (!deliveryStage.isForDriver())
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);

        final var deliveryRequestAssignment = deliveryRequestAssignmentRepository.findById(setDeliveryRequestStageDto.getDeliveryRequestAssignmentId())
                .orElseThrow();

        if (!Objects.equals(deliveryRequestAssignment.getDriverUserId(), userEntity.getId()))
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);

        final var query = Query.query(Criteria.where("id").is(deliveryRequestAssignment.getDeliveryRequestId()));
        final var update = Update.update(DELIVERY_STAGE_ID, deliveryStage.getId());

        mongoTemplate.updateFirst(query, update, DeliveryRequestDocument.class);
        // TODO notify who ever
    }

    public void setDeliveryRequestStageByAdmin(@NotNull SetDeliveryRequestStageDto setDeliveryRequestStageDto) {
        final var deliveryStage = deliveryStageService.findDeliveryStageById(setDeliveryRequestStageDto.getDeliveryStageId())
                .orElseThrow();

        if (!deliveryStage.isForDriver())
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);

        final var deliveryRequestAssignment = deliveryRequestAssignmentRepository.findById(setDeliveryRequestStageDto.getDeliveryRequestAssignmentId())
                .orElseThrow();

        final var query = Query.query(Criteria.where("id").is(deliveryRequestAssignment.getDeliveryRequestId()));
        final var update = Update.update(DELIVERY_STAGE_ID, deliveryStage.getId());

        mongoTemplate.updateFirst(query, update, DeliveryRequestDocument.class);
        // TODO notify who ever
    }

    public void declineDeliveryRequestAssignment(@NotNull DeclineDeliveryRequestAssignmentDto declineDeliveryRequestAssignmentDto) {
        final var deliveryRequestAssignment = deliveryRequestAssignmentRepository.findById(declineDeliveryRequestAssignmentDto.getId())
                .orElseThrow();

        // INFO not declined, accepted and created by you
        if (!deliveryRequestAssignment.isDeclined() &&
                deliveryRequestAssignment.isAccepted() &&
                Objects.equals(deliveryRequestAssignment.getCreatorId(), userEntity.getId()))
            return;

        final var query = Query.query(Criteria.where("id").is(declineDeliveryRequestAssignmentDto.getId()));
        final var update = Update.update(DECLINED, true)
                .set(ACCEPTED, false);

        mongoTemplate.updateMulti(query, update, DeliveryRequestAssignmentDocument.class);
        // TODO notification
    }

    public void acceptDeliveryRequestAssignment(@NotNull AcceptDeliveryRequestAssignmentDto acceptDeliveryRequestAssignmentDto) {
        final var deliveryRequestAssignment = deliveryRequestAssignmentRepository.findById(acceptDeliveryRequestAssignmentDto.getId())
                .orElseThrow();

        // INFO not accepted, not declined and created by you
        if (!deliveryRequestAssignment.isAccepted() &&
                !deliveryRequestAssignment.isDeclined() &&
                Objects.equals(deliveryRequestAssignment.getCreatorId(), userEntity.getId()))
            return;

        final var query = Query.query(Criteria.where("id").is(acceptDeliveryRequestAssignmentDto.getId()));
        final var update = Update.update(ACCEPTED, true)
                .set(DECLINED, false);

        mongoTemplate.updateMulti(query, update, DeliveryRequestAssignmentDocument.class);
        // TODO notification
    }

    @Transactional
    public void reAssignDeliveryRequest(@NotNull ReAssignDeliveryRequestDto reAssignDeliveryRequestDto) {
        final var driverProfile = driverProfileService.findDriverProfileById(reAssignDeliveryRequestDto.getDriverProfileId());

        final var query = Query.query(Criteria.where(DELIVERY_REQUEST_ID).is(reAssignDeliveryRequestDto.getDeliveryRequestId()));
        final var update = Update.update(ACCEPTED, false)
                .set(DECLINED, true);

        mongoTemplate.updateMulti(query, update, DeliveryRequestAssignmentDocument.class);

        deliveryRequestAssignmentRepository.save(DeliveryRequestAssignmentDocument.builder()
                .accepted(false)
                .declined(false)
                .deliveryRequestId(reAssignDeliveryRequestDto.getDeliveryRequestId())
                .driverUserId(driverProfile.getUserId())
                .driverProfileId(driverProfile.getId())
                .creatorId(userEntity.getId())
                .build());

        mongoTemplate.updateFirst(
                Query.query(Criteria.where("id").is(reAssignDeliveryRequestDto.getDeliveryRequestId())),
                Update.update(DRIVER_PROFILE_ID, reAssignDeliveryRequestDto.getDriverProfileId()),
                DeliveryRequestDocument.class);

        // TODO notify driver in-app or so
    }

//    public List<DeliveryRequestDto> getMyDeliveryRequests(@NotNull PaginatedRequestDto paginatedRequestDto) {
//
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        if (auth == null || !auth.isAuthenticated()) {
//            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated");
//        }
//
//        var userEmail = auth.getName();
//        var userEntity = userService.findUserByEmail(userEmail)
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));
//        final var pageNumber = paginatedRequestDto.getPageNumber();
//        final var pageSize = paginatedRequestDto.getPageSize();
//
//        final var convertToTimeStage = addFields()
//                .addField("time")
//                .withValueOf(ConvertOperators.ToLong.toLong("time"))
//                .build();
//        final var matchStage = match(Criteria.where(CREATOR_ID)
//                .is(userEntity.getId()));
//        final var deliveryStagesLookupStage = getDeliveryStageLookupStage();
//        final var addFieldStage = addFields().addField("deliveryStage")
//                .withValueOf(ArrayOperators.ArrayElemAt.arrayOf(DELIVERY_STAGES)
//                        .elementAt(0))
//                .build();
//        final var sortStage = sort(Sort.by("time").descending());
//        final var skipStage = skip(pageNumber.longValue() * pageSize.longValue());
//        final var limitStage = limit(pageSize);
//
//        final TypedAggregation<DeliveryRequestDocument> aggregation = newAggregation(DeliveryRequestDocument.class,
//                convertToTimeStage,
//                matchStage,
//                deliveryStagesLookupStage,
//                addFieldStage,
//                sortStage,
//                skipStage,
//                limitStage).withOptions(AggregationOptions.builder()
//                .allowDiskUse(true)
//                .build());
//
//        return mongoTemplate.aggregate(aggregation, DeliveryRequestDto.class)
//                .getMappedResults();
//    }

    public List<DeliveryRequestDto> getMyDeliveryRequests(@NotNull PaginatedRequestDto paginatedRequestDto) {

        // Authenticate the user
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated");
        }

        var userEmail = auth.getName();
        var userEntity = userService.findUserByEmail(userEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));

        final var pageNumber = paginatedRequestDto.getPageNumber();
        final var pageSize = paginatedRequestDto.getPageSize();

        // Build the MongoDB aggregation pipeline
//        final var convertToTimeStage = addFields()
//                .addField("time")
//                .withValueOf(ConvertOperators.ToLong.toLong("time"))
//                .build();

        // Match stage to find requests created by the authenticated user
        final var matchStage = match(Criteria.where(CREATOR_ID)
                .is(userEntity.getId()));

        // Lookup stages to enrich the result
//        final var deliveryStagesLookupStage = getDeliveryStageLookupStage();
//        final var addFieldStage = addFields()
//                .addField("deliveryStage")
//                .withValueOf(ArrayOperators.ArrayElemAt.arrayOf(DELIVERY_STAGES).elementAt(0))
//                .build();

//        // Sorting, paging, and limiting the results
//        final var sortStage = sort(Sort.by("time").descending());
//        final var skipStage = skip(pageNumber.longValue() * pageSize.longValue());
//        final var limitStage = limit(pageSize);

        // Full aggregation pipeline setup
        final TypedAggregation<DeliveryRequestDocument> aggregation = newAggregation(
                DeliveryRequestDocument.class,
//                convertToTimeStage,
                matchStage
//                deliveryStagesLookupStage,
//                addFieldStage,
//                sortStage,
//                skipStage,
//                limitStage
        ).withOptions(AggregationOptions.builder()
                .allowDiskUse(true)
                .build());

        // Execute aggregation and return mapped results as DTOs
        return mongoTemplate.aggregate(aggregation, DeliveryRequestDto.class)
                .getMappedResults();
    }


    private LookupOperation getDeliveryStageLookupStage() {
        return lookup().from(DELIVERY_STAGES)
                .let(VariableOperators.Let.ExpressionVariable.newVariable(DELIVERY_STAGE_ID)
                        .forField(DELIVERY_STAGE_ID))
                .pipeline(match(Criteria.expr(() ->
                        Document.parse("{ '$eq': [{  $toString: '$_id'}, '$$deliveryStageId'] }"))))
                .as(DELIVERY_STAGES);
    }

    private LookupOperation getDeliveryRequestAssignmentsLookupStage() {
        return lookup().from("deliveryRequestAssignments")
                .let(VariableOperators.Let.ExpressionVariable.newVariable(DELIVERY_REQUEST_ID)
                        .forField(DELIVERY_REQUEST_ID))
                .pipeline(match(Criteria.expr(() ->
                                Document.parse("{ '$eq': ['$deliveryRequestId', '$$deliveryRequestId'] }"))
                        .and("")))
                .as("deliveryRequestAssignments");
    }

    private LookupOperation getDeliveryRequestLookupStage() {
        return lookup().from(DELIVERY_REQUESTS)
                .let(VariableOperators.Let.ExpressionVariable.newVariable(DELIVERY_REQUEST_ID)
                        .forField(DELIVERY_REQUEST_ID))
                .pipeline(match(Criteria.expr(() ->
                                Document.parse("{ '$eq': [{ $toString: '$_id' }, '$$deliveryRequestId'] }"))
                        .and("")))
                .as(DELIVERY_REQUESTS);
    }

    // INFO https://www.baeldung.com/java-find-distance-between-points
    // INFO except there is a request to save draft
    @Transactional
    public SubmitDeliveryRequestResponseDto submitDeliveryRequest(@Valid @NotNull SubmitDeliveryRequestDto submitDeliveryRequestDto) throws MessagingException, IOException {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated");
        }

        var userEmail = auth.getName();
        var userEntity = userService.findUserByEmail(userEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));
        final var deliveryStage = deliveryStageService.findDeliveryStageAtInitial()
                .orElseThrow();

        final var pickup = submitDeliveryRequestDto.getPickup();
        final var destination = submitDeliveryRequestDto.getDestination();

        // INFO https://www.baeldung.com/java-random-string
        final var trackingId = RandomStringUtils.randomAlphanumeric(7);
        final var deliveryRequest = deliveryRequestRepository.save(DeliveryRequestDocument.builder()
                .trackingId(trackingId)
                .deliveryStageId(deliveryStage.getId())
                .pickup(pickup)
                .destination(destination)
                .creatorId(userEntity.getId())
                .build());
        final var driverProfile = driverProfileService.findDriverProfileById(deliveryRequest.getDriverProfileId());

        deliveryRequestAssignmentRepository.save(DeliveryRequestAssignmentDocument.builder()
                        .accepted(false)
                        .declined(false)
                        .deliveryRequestId(deliveryRequest.getId())
                        .driverUserId(driverProfile.getUserId())
                        .driverProfileId(deliveryRequest.getDriverProfileId())
                        .creatorId(userEntity.getId())
                .build());

//        final var closestDeliveryLocation = locationService.findLocationNearestTo(destination.getLatitude(), destination.getLongitude())
//                .orElseThrow();
//        final double distanceBetweenRequest = calculateDistanceBetweenPickupAndDestinationLocation(pickup, destination);
//        // INFO Omo, I hope this does not truncate much. I think it should be fine
//        final int amount = (int) Math.round(distanceBetweenRequest * closestDeliveryLocation.getAmount());

        // INFO debit wallet
//        walletService.debitWallet(DebitWalletDto.builder()
//                        .amount(amount)
//                .build());

        // TODO notify driver in-app using MQTT

        return SubmitDeliveryRequestResponseDto.builder()
                .id(deliveryRequest.getId())
//                .deliveryStage(deliveryStage)
                .trackingId(deliveryRequest.getTrackingId())
                .build();
    }

    // INFO FOR NOW, WE USE HAVERSINE
    // INFO this is in kilometers because of the earth's radius is in km 6731
    public double calculateDistanceBetweenPickupAndDestinationLocation(
            @NotNull DeliveryRequestPickupDto deliveryRequestPickupDto,
            @NotNull DeliveryRequestDestinationDto deliveryRequestDestinationDto) {
        final double startLatitude = deliveryRequestPickupDto.getLatitude();
        final double startLongitude = deliveryRequestPickupDto.getLongitude();

        final double endLatitude = deliveryRequestDestinationDto.getLatitude();
        final double endLongitude = deliveryRequestDestinationDto.getLongitude();

        final double destinationLatitude = Math.toRadians((endLatitude - startLatitude));
        final double destinationLongitude = Math.toRadians((endLongitude - startLongitude));

        final double a = haversine(destinationLatitude) + Math.cos(Math.toRadians(startLatitude)) * Math.cos(Math.toRadians(endLatitude)) * haversine(destinationLongitude);
        final double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS * c;
    }

    public double haversine(double val) {
        return Math.pow(Math.sin(val / 2), 2);
    }

    public List<DeliveryRequestDto> getDeliveryRequests(@NotNull FindDeliveryRequestDto findDeliveryRequestDto) {
        final var pageNumber = findDeliveryRequestDto.getPageNumber();
        final var pageSize = findDeliveryRequestDto.getPageSize();
        final var startDate = findDeliveryRequestDto.getStartDate();
        final var endDate = findDeliveryRequestDto.getEndDate();
        final var trackingId = findDeliveryRequestDto.getTrackingId();
        final var deliveryStageId = findDeliveryRequestDto.getDeliveryStageId();
        final var driverProfileId = findDeliveryRequestDto.getDriverProfileId();
        final var creatorId = findDeliveryRequestDto.getCreatorId();

        var where = new Criteria();

        if (startDate != null && endDate != null)
            where = where
                    .and("time")
                    .gte(startDate.toInstant(ZoneOffset.UTC)
                            .toEpochMilli())
                    .lte(endDate.toInstant(ZoneOffset.UTC)
                            .toEpochMilli());

        if (creatorId != null)
            where.and(CREATOR_ID)
                    .is(creatorId);

        if (trackingId != null)
            where.and("trackingId")
                    .is(trackingId);

        if (deliveryStageId != null)
            where.and(DELIVERY_STAGE_ID)
                    .is(deliveryStageId);

        if (driverProfileId != null)
            where.and(DRIVER_PROFILE_ID)
                    .is(driverProfileId);

        final var convertToTimeStage = addFields()
                .addField("time")
                .withValueOf(ConvertOperators.ToLong.toLong("time"))
                .build();
        final var matchStage = match(where);
        final var deliveryStagesLookupStage = getDeliveryStageLookupStage();
        final var deliveryRequestAssignmentsLookupStage = getDeliveryRequestAssignmentsLookupStage();
        final var sortStage = sort(Sort.by("time").descending());
        final var skipStage = skip(pageNumber.longValue() * pageSize.longValue());
        final var limitStage = limit(pageSize);

        final TypedAggregation<DeliveryRequestDocument> aggregation = newAggregation(DeliveryRequestDocument.class,
                convertToTimeStage,
                matchStage,
                deliveryStagesLookupStage,
                deliveryRequestAssignmentsLookupStage,
                sortStage,
                skipStage,
                limitStage).withOptions(AggregationOptions.builder()
                .allowDiskUse(true)
                .build());

        return mongoTemplate.aggregate(aggregation, DeliveryRequestDto.class)
                .getMappedResults();
    }

    public List<DeliveryRequestAssignmentDto> getDeliveryRequestAssignments(@NotNull FindDeliveryRequestAssignmentDto findDeliveryRequestAssignmentDto) {
        final var pageNumber = findDeliveryRequestAssignmentDto.getPageNumber();
        final var pageSize = findDeliveryRequestAssignmentDto.getPageSize();
        final var accepted = findDeliveryRequestAssignmentDto.getAccepted();
        final var declined = findDeliveryRequestAssignmentDto.getDeclined();
        final var deliveryRequestId = findDeliveryRequestAssignmentDto.getDeliveryRequestId();
        final var driverProfileId = findDeliveryRequestAssignmentDto.getDriverProfileId();
        final var creatorId = findDeliveryRequestAssignmentDto.getCreatorId();

        var where = new Criteria();

        if (Boolean.TRUE.equals(accepted))
            where = where.and(ACCEPTED)
                    .is(accepted);

        if (Boolean.TRUE.equals(declined))
            where = where.and(DECLINED)
                    .is(declined);

        if (deliveryRequestId != null)
            where = where.and(DELIVERY_REQUEST_ID)
                    .is(deliveryRequestId);

        if (driverProfileId != null)
            where = where.and(DRIVER_PROFILE_ID)
                    .is(driverProfileId);

        if (creatorId != null)
            where = where.and(CREATOR_ID)
                    .is(creatorId);

        final var convertToTimeStage = addFields()
                .addField("time")
                .withValueOf(ConvertOperators.ToLong.toLong("time"))
                .build();
        final var matchStage = match(where);
        final var deliveryRequestsLookupStage = getDeliveryRequestLookupStage();
        final var addFieldStage = addFields().addField("deliveryRequest")
                .withValueOf(ArrayOperators.ArrayElemAt.arrayOf(DELIVERY_REQUESTS)
                        .elementAt(0))
                .build();
        final var sortStage = sort(Sort.by("time").descending());
        final var skipStage = skip(pageNumber.longValue() * pageSize.longValue());
        final var limitStage = limit(pageSize);

        final TypedAggregation<DeliveryRequestAssignmentDocument> aggregation = newAggregation(DeliveryRequestAssignmentDocument.class,
                convertToTimeStage,
                matchStage,
                deliveryRequestsLookupStage,
                addFieldStage,
                sortStage,
                skipStage,
                limitStage).withOptions(AggregationOptions.builder()
                .allowDiskUse(true)
                .build());

        return mongoTemplate.aggregate(aggregation, DeliveryRequestAssignmentDto.class)
                .getMappedResults();
    }
}
