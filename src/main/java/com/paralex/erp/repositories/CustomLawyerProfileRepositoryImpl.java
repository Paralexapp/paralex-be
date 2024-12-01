package com.paralex.erp.repositories;

import com.paralex.erp.entities.LawyerProfileEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CustomLawyerProfileRepositoryImpl implements CustomLawyerProfileRepository {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public CustomLawyerProfileRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }


    @Override
    public List<LawyerProfileEntity> searchLawyers(double latitude, double longitude, int limit, int offset) {
        // Create a Point using the latitude and longitude
        Point point = new Point(longitude, latitude);  // Note: Point is (longitude, latitude)

        // Define the geoCriteria for proximity search
        Criteria geoCriteria = Criteria.where("location").nearSphere(point);

        // Create the query with pagination
        Query query = new Query(geoCriteria).limit(limit).skip(offset);

        return mongoTemplate.find(query, LawyerProfileEntity.class);
    }

    @Override
    public void enableProfileByUserId(String userId) {
        Query query = new Query(Criteria.where("userId").is(userId));
        Update update = new Update().set("status", true);
        mongoTemplate.updateFirst(query, update, LawyerProfileEntity.class);
    }

    @Override
    public void disableProfileByUserId(String userId) {
        Query query = new Query(Criteria.where("userId").is(userId));
        Update update = new Update().set("status", false);
        mongoTemplate.updateFirst(query, update, LawyerProfileEntity.class);
    }

    @Override
    public Page<LawyerProfileEntity> findAll(Criteria criteria, PageRequest pageable) {
        // Create a query object with the provided criteria
        Query query = new Query(criteria);

        // Apply pagination
        query.with(pageable);

        // Get total count for pagination purposes
        long totalCount = mongoTemplate.count(query, LawyerProfileEntity.class);

        // Apply pagination (limit & skip)
        query.limit(pageable.getPageSize());  // Set page size (limit)
        query.skip(pageable.getOffset());  // Set page offset (skip)

        // Execute the query and retrieve the results
        List<LawyerProfileEntity> results = mongoTemplate.find(query, LawyerProfileEntity.class);

        // Return the results wrapped in a Page object
        return new PageImpl<>(results, pageable, totalCount);
    }
}
