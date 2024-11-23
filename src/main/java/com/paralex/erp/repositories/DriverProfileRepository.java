package com.paralex.erp.repositories;

import com.paralex.erp.entities.DriverProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DriverProfileRepository extends JpaRepository<DriverProfileEntity, String>, JpaSpecificationExecutor<DriverProfileEntity> {
    @Modifying
    @Query("""
            UPDATE DriverProfileEntity dpe
                SET dpe.status = false
                WHERE dpe.userId = :userId
            """)
    void enableProfileByEmail(@Param("userId")String userId);

    @Modifying
    @Query("""
            UPDATE DriverProfileEntity dpe
                SET dpe.status = true
                WHERE dpe.userId = :userId
            """)
    void disableProfileByEmail(@Param("userId") String userId);
}
