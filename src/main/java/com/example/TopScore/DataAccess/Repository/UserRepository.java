package com.example.TopScore.DataAccess.Repository;

import com.example.TopScore.DataAccess.Entities.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, UUID>{
}
