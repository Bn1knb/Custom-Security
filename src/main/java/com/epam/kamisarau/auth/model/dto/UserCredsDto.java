package com.epam.kamisarau.auth.model.dto;

import com.epam.kamisarau.auth.model.UserModel;
import lombok.Data;

import java.io.Serializable;

@Data
public class UserCredsDto implements Serializable {
    private String username;
    private String password;

    public UserCredsDto getUserCredsFromUser(UserModel user) {
        this.username = user.getUsername();
        this.password = user.getPassword();
        return this;
    }
}
