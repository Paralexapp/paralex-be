package com.paralex.erp.repositories;

import com.paralex.erp.entities.LocationEntity;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LocationRepository extends MongoRepository<LocationEntity, String> {

    List<LocationEntity> findByLocationNear(
                                             @Param("point") Point point,
                                             @Param("maxDistance") Distance maxDistance,
                                             @Param("limit") int limit,
                                             @Param("skip") int skip
    );
}