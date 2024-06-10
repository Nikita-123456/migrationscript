package com.migration.example.migrationscript.repository;

import com.migration.example.migrationscript.model.UserData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMongoRepository extends MongoRepository<UserData, Integer> {
    List<UserData> findByUserIdIn(List<Integer> ids);
}