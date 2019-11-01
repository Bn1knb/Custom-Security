package com.bn1knb.newsblog.dto;

import com.bn1knb.newsblog.model.User;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistrationDto implements Serializable {

    @NotBlank
    @Size(min = 3, max = 10)
    private String username;
    @NotBlank
    @Size(min = 6, max = 25)
    private String password;
    @NotBlank()
    @Size(min = 1)
    private String firstName;
    @NotBlank
    @Size(min = 1)
    private String lastName;
    @NotBlank
    @Email
    private String email;

    @ApiModelProperty(hidden = true)
    public User toUser() {
        return User.builder()
                .username(username)
                .password(password)
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .build();
    }
}
