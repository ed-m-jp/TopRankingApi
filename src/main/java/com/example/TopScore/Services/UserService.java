package com.example.TopScore.Services;

import com.example.TopScore.DataAccess.Entities.UserEntity;
import com.example.TopScore.DataAccess.Repository.UserRepository;
import com.example.TopScore.Services.Interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements IUserService {

    private UserRepository repository;

    @Autowired
    public UserService(UserRepository repository)
    {
        this.repository = repository;
    }

    public Optional<UserEntity> getUserById(long userId)
    {
        return repository.findById(userId);
    }

}
