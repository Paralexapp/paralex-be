package com.paralex.erp.repositories;

import com.paralex.erp.entities.LocationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface LocationRepository extends JpaRepository<LocationEntity, String> {
    @Query(value = """
                SELECT * FROM locations le
                    ORDER BY le.location <-> ST_MakePoint(:latitude, :longitude)::geography
                    LIMIT :limit
                    OFFSET :offset
            """, nativeQuery = true)
    Optional<LocationEntity> findLocationNearestTo(
            @Param("latitude") double latitude,
            @Param("longitude") double longitude,
            @Param("limit") int limit,
            @Param("offset") int offset);
}
