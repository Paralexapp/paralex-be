package com.paralex.erp.repositories;

import com.paralex.erp.entities.LawPracticeAreaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface LawPracticeAreaRepository extends JpaRepository<LawPracticeAreaEntity, String>, JpaSpecificationExecutor<LawPracticeAreaEntity> {
}
