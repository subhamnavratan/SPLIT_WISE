package com.ZETA.KN.repository;

import com.ZETA.KN.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface  UserRepository extends MongoRepository<User ,String >
{
    Optional<User> findByPhone(Long phone);

}