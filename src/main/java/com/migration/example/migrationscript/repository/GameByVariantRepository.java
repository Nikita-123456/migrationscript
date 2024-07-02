package com.migration.example.migrationscript.repository;

import com.migration.example.migrationscript.model.GameByVariant;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameByVariantRepository extends CrudRepository<GameByVariant, Integer> {
    List<GameByVariant> findByUserIdIn(List<Integer> userIds);

    @Query("select userid from gamebyvariant")
    List<Integer> findDistinctUserIdByUserId();

}
