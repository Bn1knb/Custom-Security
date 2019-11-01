package com.epam.kamisarau.auth.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.SERVICE_UNAVAILABLE)
public class RegistrationFailedException extends RuntimeException {
    public RegistrationFailedException() {
        super("Registration failed");
    }
}
