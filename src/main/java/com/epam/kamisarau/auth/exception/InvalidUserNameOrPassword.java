package com.epam.kamisarau.auth.exception;

public class InvalidUserNameOrPassword extends RuntimeException {
    public InvalidUserNameOrPassword() {
        super("Invalid Username or password");
    }
}
