package com.epam.kamisarau.auth.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NoTokenFound extends RuntimeException {
    public NoTokenFound() {
        super("No token Found");
    }
}
