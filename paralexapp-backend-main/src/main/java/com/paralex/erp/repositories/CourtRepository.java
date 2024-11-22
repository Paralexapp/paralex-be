package com.paralex.erp.repositories;

import com.paralex.erp.entities.CourtEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CourtRepository extends JpaRepository<CourtEntity, String>, JpaSpecificationExecutor<CourtEntity> {
}
