package com.epam.kamisarau.auth.model.dto;

import com.epam.kamisarau.auth.model.RoleModel;
import com.epam.kamisarau.auth.model.State;
import com.epam.kamisarau.auth.model.UserModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

@Data
public class UserRegistrationDto implements Serializable {
    @NotEmpty(message = "Please provide a username")
    @Size(min = 2, max = 14)
    private String username;
    @NotEmpty(message = "Please provide a password")
    @Size(min = 8, message = "password must be at least 8 characters long")
    private String password;
    @NotEmpty(message = "Please provide a surname")
    private String surname;
    @NotEmpty(message = "Please provide a name")
    private String name;
    @NotEmpty(message = "Please provide an email")
    private String email;

    @ApiModelProperty(hidden = true)
    public UserModel getUserModelFromRegistrationDto() {
        return new UserModel()
                .setState(State.ACTIVE)
                .setId(null)
                .setUsername(username)
                .setPassword(password)
                .setFirstName(name)
                .setLastName(surname)
                .setRole(RoleModel.ROLE_USER)
                .setEmail(email)
                .setRegisteredAt(new Date());
    }
}
