package com.epam.kamisarau.auth.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class DatabaseConnectionFailedException extends RuntimeException {
    public DatabaseConnectionFailedException() {
        super("Database connection failed");
    }
}
