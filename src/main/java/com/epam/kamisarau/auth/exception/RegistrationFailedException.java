package com.epam.kamisarau.auth.exception;

public class RegistrationFailedException extends RuntimeException {
    public RegistrationFailedException() {
        super("Registration failed");
    }
}
