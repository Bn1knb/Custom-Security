package com.epam.kamisarau.auth.exception;

public class NoTokenFound extends RuntimeException {
    public NoTokenFound() {
        super("No token Found");
    }
}
