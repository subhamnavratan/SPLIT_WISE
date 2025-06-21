package com.ZETA.KN.repository;

import com.ZETA.KN.model.Members;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface MembersRepository extends MongoRepository<Members, String> {
    Optional<Members> findByGroupName(String groupName);
}



