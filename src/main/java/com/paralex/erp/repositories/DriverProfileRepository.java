package com.paralex.erp.repositories;

import com.paralex.erp.entities.DriverProfileEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;

import java.util.List;
import java.util.Optional;

@Repository
public interface DriverProfileRepository extends MongoRepository<DriverProfileEntity, String> {

    // Custom query methods
    Optional<DriverProfileEntity> findByUserId(String userId);

    List<DriverProfileEntity> findByIdIn(List<String> driverProfileIds);

//    @Query("{ 'location': { $near: { $geometry: { type: 'Point', coordinates: [?0, ?1] }, $maxDistance: ?2 } } }")
    List<DriverProfileEntity> findByLocation(Point point, Distance maxDistance, int limit);
    List<DriverProfileEntity> findByLocationNear(Point point, Distance maxDistance, Pageable pageable);

}