package com.paralex.erp.repositories;

import com.paralex.erp.entities.LocationEntity;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;

import java.util.List;

public interface CustomLocationRepository {
    List<LocationEntity> findLocationsNear(Point point, Distance maxDistance, int limit, int skip);
}
