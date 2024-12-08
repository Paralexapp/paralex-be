package com.paralex.erp.configs;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApiKeyRepository extends MongoRepository<ApiKey,String> {
    Optional<ApiKey> findByInstitutionName(String institutionName);

    Optional<ApiKey> findByIdAndIsEnabled(String id, Boolean isEnabled);
    Optional<ApiKey> findByInstitutionNameAndIsEnabled(String institutionName, Boolean isEnabled);
    List<ApiKey> findAllByIsEnabled(Boolean isEnabled);
    List<ApiKey> findAllByInstitutionName(String institutionName);
    List<ApiKey> findAllByInstitutionNameAndIsEnabled(String institutionName, Boolean isEnabled);

    Optional<ApiKey> findByProdKeyAndIsEnabled(String key,Boolean isEnabled);
    Optional<ApiKey> findByTestKey(String key);
}
