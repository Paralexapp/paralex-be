package com.paralex.erp.repositories;

import com.paralex.erp.entities.LawyerPracticeAreaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface LawyerPracticeAreaRepository extends JpaRepository<LawyerPracticeAreaEntity, String>, JpaSpecificationExecutor<LawyerPracticeAreaEntity> {
}
