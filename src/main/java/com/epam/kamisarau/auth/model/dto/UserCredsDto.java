package com.epam.kamisarau.auth.model.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserCredsDto implements Serializable {
    private String username;
    private String password;
}
