package com.example.TopScore.DataAccess.Repository;

import com.example.TopScore.DataAccess.Entities.ScoreEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ScoreRepository extends PagingAndSortingRepository<ScoreEntity, Long> {

    @Query("SELECT p FROM ScoreEntity p WHERE p.normalizedPlayer = upper(:player)")
    Optional<List<ScoreEntity>> findRecordsByName(@Param("player") String player);
}


