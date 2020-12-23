package com.example.TopScore.Exception;

public class ScoreNotFoundException extends RuntimeException {
    public ScoreNotFoundException(Long id) {
        super("Could not find score with id [" + id + "]");
    }
}

