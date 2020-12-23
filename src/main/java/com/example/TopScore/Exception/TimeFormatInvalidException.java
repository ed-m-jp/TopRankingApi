package com.example.TopScore.Exception;

public class TimeFormatInvalidException extends RuntimeException {
    public TimeFormatInvalidException()
    {
        super("The format of the time is not valid please use this format [dd/MM/yyyy hh:mm:ss]");
    }
}
