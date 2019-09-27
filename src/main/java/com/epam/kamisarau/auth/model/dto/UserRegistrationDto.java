package com.epam.kamisarau.auth.model.dto;

import com.epam.kamisarau.auth.model.UserModel;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class UserRegistrationDto implements Serializable {
    private String username;
    private String password;
    private String name;
    private String surname;

    public UserModel getUserModelFromRegistrationDto() {
        return new UserModel()
                .setActive(true)
                .setId(null)
                .setUsername(username)
                .setPassword(password)
                .setName(name)
                .setSurname(surname)
                .setRegisteredAt(new Date());
    }
}
