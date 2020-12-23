package com.example.TopScore.Exception;

public class PlayerHistoryNotFoundException extends RuntimeException {
    public PlayerHistoryNotFoundException(String player)
    {
        super("No data found for player [" + player + "]");
    }
}