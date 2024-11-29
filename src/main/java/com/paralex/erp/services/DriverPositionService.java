package com.paralex.erp.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paralex.erp.documents.DriverPositionDocument;
import com.paralex.erp.dtos.FindDriversNearbyDto;
import com.paralex.erp.dtos.SaveDriverPositionDto;
import com.paralex.erp.dtos.SoughtDriver;
import com.paralex.erp.entities.DriverProfileEntity;
import com.paralex.erp.repositories.DriverPositionRepository;
import com.paralex.erp.repositories.DriverProfileRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.eclipse.paho.mqttv5.client.MqttClient;
import org.eclipse.paho.mqttv5.common.MqttException;
import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.eclipse.paho.mqttv5.common.MqttSubscription;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.AggregationOptions;
import org.springframework.data.mongodb.core.aggregation.ConvertOperators;
import org.springframework.data.mongodb.core.aggregation.Fields;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.NearQuery;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Objects;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@RequiredArgsConstructor
@Validated
@Service
@Log4j2
public class DriverPositionService {
    public static final String DRIVER_PROFILE_ID = "driverProfileId";

    private final DriverPositionRepository driverPositionRepository;
    private final DriverProfileRepository driverProfileRepository;
    private final MongoTemplate mongoTemplate;
//    private final MqttClient mqttClient;
    private ObjectMapper objectMapper;

//    @Value("${PARALEXAPP_DRIVER_POSITION_TOPIC}")
//    private String driverPositionTopic;
//
//    public void subscribeForDriverPosition() throws MqttException {
//        mqttClient.subscribe(List.of(new MqttSubscription("$share/service-group/" + driverPositionTopic, 2))
//                .toArray(new MqttSubscription[0]));
//    }

    public void saveDriverPosition(@NotNull SaveDriverPositionDto saveDriverPositionDto) {
        driverPositionRepository.save(DriverPositionDocument.builder()
                .location(new GeoJsonPoint(new Point(saveDriverPositionDto.getLatitude(), saveDriverPositionDto.getLongitude())))
                .driverProfileId(saveDriverPositionDto.getDriverProfileId())
                .build());
    }

    public void processMessage(@NotNull MqttMessage mqttMessage) throws IOException {
        final var driverPosition = objectMapper.readValue(mqttMessage.getPayload(), SaveDriverPositionDto.class);

        saveDriverPosition(driverPosition);
    }

    // TODO get driver status API to remediate SPARK unavailability
    public List<DriverProfileEntity> findDriversNearby(@NotNull FindDriversNearbyDto findDriversNearbyDto) {
        // TODO should update driver status if no heartbeat for 15 minutes
        final var latitude = findDriversNearbyDto.getLatitude();
        final var longitude = findDriversNearbyDto.getLongitude();

        final var geoNearStage = geoNear(NearQuery.near(new GeoJsonPoint(new Point(latitude, longitude)))
                .inKilometers()
                .maxDistance(Objects.requireNonNullElse(findDriversNearbyDto.getRadius(), 5d))
                .spherical(true), "calculatedDistance");
        final var sortStage = sort(Sort.by(DRIVER_PROFILE_ID, "time").descending());
        final var convertToTimeStage = addFields()
                .addField("time")
                .withValueOf(ConvertOperators.ToLong.toLong("time"))
                .build();
        final var fiveMinutesAgo = LocalDateTime.now()
                .minusMinutes(5)
                .toInstant(ZoneOffset.UTC).toEpochMilli();
        final var matchStage = match(Criteria.where("time").gte(fiveMinutesAgo));

        final var groupStage = group(Fields.fields(DRIVER_PROFILE_ID))
                .last(DRIVER_PROFILE_ID)
                .as(DRIVER_PROFILE_ID)
                .last("location")
                .as("lastPosition")
                .last("time")
                .as("time");

        final TypedAggregation<DriverPositionDocument> aggregationStages = newAggregation(DriverPositionDocument.class,
                geoNearStage,
                convertToTimeStage,
                sortStage,
                matchStage,
                groupStage)
                .withOptions(AggregationOptions.builder()
                        .allowDiskUse(true)
                        .build());

        final var driverPositions = mongoTemplate.aggregate(aggregationStages, SoughtDriver.class)
                .getMappedResults();

        final var driverProfiles = driverPositions.stream()
                .map(SoughtDriver::getDriverProfileId)
                .toList();

        return driverProfileRepository.findByIdIn(driverProfiles)
                .stream()
                .filter(item -> !item.isOffline())
                .toList();
    }
}
