package com.example.TopScore.Shared.helper;

import java.util.UUID;

public class UuidHelper {

    public static String GetUuid()
    {
        // Creating a random UUID (Universally unique identifier).
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }
}
