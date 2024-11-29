package com.paralex.erp.repositories;

import com.paralex.erp.entities.LawFirmPracticeAreaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface LawFirmPracticeAreaRepository extends JpaRepository<LawFirmPracticeAreaEntity, String>, JpaSpecificationExecutor<LawFirmPracticeAreaEntity> {
}
