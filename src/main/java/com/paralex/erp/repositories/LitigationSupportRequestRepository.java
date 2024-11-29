package com.paralex.erp.repositories;

import com.paralex.erp.entities.LitigationSupportRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LitigationSupportRequestRepository extends JpaRepository<LitigationSupportRequestEntity, String>, JpaSpecificationExecutor<LitigationSupportRequestEntity> {
    @Modifying
    @Query(value = """
        UPDATE LitigationSupportRequestEntity ltre
            SET ltre.paid = :paid
            WHERE ltre.paymentRequestCode = :paymentRequestCode
        """)
    void paidForBailBond(
            @Param("paymentRequestCode") String paymentRequestCode,
            @Param("paid") boolean paid);
}
