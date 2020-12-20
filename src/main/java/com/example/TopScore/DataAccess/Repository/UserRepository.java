package com.example.TopScore.DataAccess.Repository;

import com.example.TopScore.DataAccess.Entities.UserEntity;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends PagingAndSortingRepository<UserEntity, Long> {
}
