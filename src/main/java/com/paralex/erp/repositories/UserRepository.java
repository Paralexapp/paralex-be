package com.paralex.erp.repositories;

import com.paralex.erp.entities.UserEntity;
import com.paralex.erp.enums.UserType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<UserEntity, String> {

    // Find a user by email
    Optional<UserEntity> findByEmail(String email);

    // Find a user by phone number
    Optional<UserEntity> findByPhoneNumber(String phoneNumber);

    @Query("{ 'userType': ?0 }")
    List<UserEntity> findAllByUserType(UserType userType);

    List<UserEntity> findByUserType(UserType userType);

}
