package com.epam.kamisarau.auth.exception;

public class TokenNotStored extends RuntimeException {
    public TokenNotStored(String message) {
        super("Token storing failed :" + message);
    }
}
