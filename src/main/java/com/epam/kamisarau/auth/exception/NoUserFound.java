package com.epam.kamisarau.auth.exception;

public class NoUserFound extends RuntimeException {
    public NoUserFound() {
        super("No user found");
    }
}
