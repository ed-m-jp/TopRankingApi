package com.example.TopScore.Exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long id) {
        super("Could not find user with id [" + id + "]");
    }
}
