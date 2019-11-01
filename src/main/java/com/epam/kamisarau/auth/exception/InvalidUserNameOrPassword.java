package com.epam.kamisarau.auth.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidUserNameOrPassword extends RuntimeException {
    public InvalidUserNameOrPassword() {
        super("Invalid Username or password");
    }
}
