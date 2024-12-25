package com.paralex.erp.repositories;

import com.paralex.erp.entities.LocationEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CustomLocationRepositoryImpl implements CustomLocationRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<LocationEntity> findLocationsNear(Point point, Distance maxDistance, int limit, int skip) {
        Query query = new Query()
                .addCriteria(Criteria.where("location").near(point).maxDistance(maxDistance.getValue() / 6378.1)) // Max distance in radians
                .limit(limit)
                .skip(skip);

        return mongoTemplate.find(query, LocationEntity.class);
    }
}
