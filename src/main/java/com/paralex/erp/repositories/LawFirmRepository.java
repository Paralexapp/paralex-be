package com.paralex.erp.repositories;

import com.paralex.erp.entities.LawFirmEntity;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LawFirmRepository extends MongoRepository<LawFirmEntity, String> {

    @Autowired
    MongoTemplate mongoTemplate = null;

    // Geospatial Query to search LawFirms by proximity to a latitude and longitude
    default List<LawFirmEntity> searchLawFirms(double latitude, double longitude, int limit, int offset) {
        // Create a Point using the latitude and longitude
        Point point = new Point(longitude, latitude);  // Note: Point is (longitude, latitude)

        // Define the geoCriteria for proximity search
        Criteria geoCriteria = Criteria.where("location").nearSphere(point);

        // Create the query with pagination
        Query query = new Query(geoCriteria).limit(limit).skip(offset);

        return mongoTemplate.find(query, LawFirmEntity.class);
    }

    // Find LawFirm by Creator ID
    Optional<LawFirmEntity> findByCreatorId(String creatorId);
}
