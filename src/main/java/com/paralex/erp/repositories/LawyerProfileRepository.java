package com.paralex.erp.repositories;

import com.paralex.erp.entities.LawyerProfileEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LawyerProfileRepository extends JpaRepository<LawyerProfileEntity, String>, JpaSpecificationExecutor<LawyerProfileEntity> {
    @Modifying
    @Query("""
            UPDATE LawyerProfileEntity dpe
                SET dpe.status = false
                WHERE dpe.userId = :userId
            """)
    void enableProfileByEmail(@Param("userId")String userId);

    @Modifying
    @Query("""
            UPDATE LawyerProfileEntity dpe
                SET dpe.status = true
                WHERE dpe.userId = :userId
            """)
    void disableProfileByEmail(@Param("userId") String userId);

    Optional<LawyerProfileEntity> findByUserId(@NotNull @NotEmpty @NotBlank String userId);

    @Query(value = """
                SELECT * FROM lawyerProfiles lps
                    ORDER BY lps.location <-> ST_MakePoint(:latitude, :longitude)::geography
                    LIMIT :limit
                    OFFSET :offset
            """, nativeQuery = true)
    List<LawyerProfileEntity> searchLawyers(
            @Param("latitude") double latitude,
            @Param("longitude") double longitude,
            @Param("limit") int limit,
            @Param("offset") int offset);
}
