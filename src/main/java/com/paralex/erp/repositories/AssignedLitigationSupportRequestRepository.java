package com.paralex.erp.repositories;

import com.paralex.erp.entities.AssignedLitigationSupportRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AssignedLitigationSupportRequestRepository extends JpaRepository<AssignedLitigationSupportRequestEntity, String>, JpaSpecificationExecutor<AssignedLitigationSupportRequestEntity> {

    @Modifying
    @Query("""
                Update AssignedLitigationSupportRequestEntity alsre
                    SET alsre.accepted = true,
                    alsre.rejected = false
                    WHERE alsre.id = :id
            """)
    void accept(@Param("id") String id);

    @Modifying
    @Query("""
                Update AssignedLitigationSupportRequestEntity alsre
                    SET alsre.accepted = false,
                    alsre.rejected = true
                    WHERE alsre.id = :id
            """)
    void reject(@Param("id") String id);
}
