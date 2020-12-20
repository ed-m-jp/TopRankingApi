package com.example.TopScore.Services.Interfaces;

import com.example.TopScore.DataAccess.Entities.UserEntity;

import java.util.Optional;

public interface IUserService {
    Optional<UserEntity> getUserById(long userId);
}
