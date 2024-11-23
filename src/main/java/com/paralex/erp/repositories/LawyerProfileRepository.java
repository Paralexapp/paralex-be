package com.paralex.erp.repositories;

import com.paralex.erp.entities.LawyerProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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
}
