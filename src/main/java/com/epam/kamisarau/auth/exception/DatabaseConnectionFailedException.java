package com.epam.kamisarau.auth.exception;

public class DatabaseConnectionFailedException extends RuntimeException {
    public DatabaseConnectionFailedException() {
        super("Database connection failed");
    }
}
