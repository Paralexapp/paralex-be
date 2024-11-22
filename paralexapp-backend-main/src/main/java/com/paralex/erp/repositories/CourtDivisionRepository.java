package com.paralex.erp.repositories;

import com.paralex.erp.entities.CourtDivisionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CourtDivisionRepository extends JpaRepository<CourtDivisionEntity, String>, JpaSpecificationExecutor<CourtDivisionEntity> {
}
