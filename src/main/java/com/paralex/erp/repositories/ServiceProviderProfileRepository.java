package com.paralex.erp.repositories;

import com.paralex.erp.entities.ServiceProviderProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ServiceProviderProfileRepository extends JpaRepository<ServiceProviderProfileEntity, String>, JpaSpecificationExecutor<ServiceProviderProfileEntity> {
}
