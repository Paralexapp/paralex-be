package com.paralex.erp.repositories;

import com.paralex.erp.entities.LawFirmMemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface LawFirmMemberRepository extends JpaRepository<LawFirmMemberEntity, String>, JpaSpecificationExecutor<LawFirmMemberEntity> {
}
