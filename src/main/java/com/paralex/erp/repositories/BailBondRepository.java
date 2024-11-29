package com.paralex.erp.repositories;

import com.paralex.erp.entities.BailBondEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BailBondRepository extends JpaRepository<BailBondEntity, String>, JpaSpecificationExecutor<BailBondEntity> {
    @Modifying
    @Query(value = """
        UPDATE BailBondEntity bbe
            SET bbe.approved = :approved
            WHERE bbe.paymentRequestCode = :paymentRequestCode
    """)
    void approveBailBondRequest(
            @Param("paymentRequestCode") String paymentRequestCode,
            @Param("approved") boolean b);

    @Modifying
    @Query(value = """
        UPDATE BailBondEntity bbe
        SET bbe.rejected = :rejected
        WHERE bbe.id = :id
    """)
    void rejectBailBondRequest(
            @Param("id") String id,
            @Param("rejected") boolean b);

    @Modifying
    @Query(value = """
        UPDATE BailBondEntity bbe
            SET bbe.paid = :paid
            WHERE bbe.paymentRequestCode = :paymentRequestCode
        """)
    void paidForBailBond(
            @Param("paymentRequestCode") String paymentRequestCode,
            @Param("paid") boolean paid);

    @Modifying
    @Query(value = """
        UPDATE BailBondEntity bbe
            SET bbe.withdrawn = :withdrawn
            WHERE bbe.id = :id
        """)
    void withdrawBailBondRequest(
            @Param("id") String id,
            @Param("withdrawn") boolean b);
}
