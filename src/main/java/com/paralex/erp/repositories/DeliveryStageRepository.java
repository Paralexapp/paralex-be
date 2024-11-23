package com.paralex.erp.repositories;

import com.paralex.erp.entities.DeliveryStageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface DeliveryStageRepository extends JpaRepository<DeliveryStageEntity, String>, JpaSpecificationExecutor<DeliveryStageEntity> {
}
