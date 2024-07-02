package com.migration.example.migrationscript.repository;

import com.migration.example.migrationscript.model.UsersWinningByGamevariant;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UsersWinningByGamevariantRepository extends CrudRepository<UsersWinningByGamevariant, Long> {

    @Query("select distinct userId from userswinningbygamevariant")
    List<Long> findUserIds();
}
