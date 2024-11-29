package com.paralex.erp.repositories;

import com.paralex.erp.entities.DriverProfileEntity;
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

public interface DriverProfileRepository extends JpaRepository<DriverProfileEntity, String>, JpaSpecificationExecutor<DriverProfileEntity> {
    @Modifying
    @Query("""
            UPDATE DriverProfileEntity dpe
                SET dpe.status = false
                WHERE dpe.userId = :userId
            """)
    void enableProfileByEmail(@Param("userId") String userId);

    @Modifying
    @Query("""
            UPDATE DriverProfileEntity dpe
                SET dpe.status = true
                WHERE dpe.userId = :userId
            """)
    void disableProfileByEmail(@Param("userId") String userId);

    List<DriverProfileEntity> findByIdIn(List<String> driverProfileIds);

    Optional<DriverProfileEntity> findByUserId(@NotNull @NotEmpty @NotBlank String userId);

}
