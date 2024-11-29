package com.paralex.erp.repositories;

import com.paralex.erp.entities.BillOfChargeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BillOfChargesRepository extends JpaRepository<BillOfChargeEntity, String> {
}
