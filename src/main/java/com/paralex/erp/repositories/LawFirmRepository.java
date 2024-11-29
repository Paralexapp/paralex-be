package com.paralex.erp.repositories;

import com.paralex.erp.entities.LawFirmEntity;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LawFirmRepository extends JpaRepository<LawFirmEntity, String> {
    @Query(value = """
                SELECT * FROM lawFirms lfs
                    ORDER BY lfs.location <-> ST_MakePoint(:latitude, :longitude)::geography
                    LIMIT :limit
                    OFFSET :offset
            """, nativeQuery = true)
    List<LawFirmEntity> searchLawFirms(
            @Param("latitude") double latitude,
            @Param("longitude") double longitude,
            @Param("limit") int limit,
            @Param("offset") int offset);

    Optional<LawFirmEntity> findByCreatorId(@NotNull String creatorId);
}
